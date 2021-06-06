package net.diyigemt.miraiboot.core;

import net.diyigemt.miraiboot.annotation.*;
import net.diyigemt.miraiboot.constant.ConstantGlobal;
import net.diyigemt.miraiboot.constant.EventHandlerType;
import net.diyigemt.miraiboot.constant.FunctionId;
import net.diyigemt.miraiboot.entity.AutoInitItem;
import net.diyigemt.miraiboot.entity.ConfigFile;
import net.diyigemt.miraiboot.entity.ExceptionHandlerItem;
import net.diyigemt.miraiboot.permission.CheckPermission;
import net.diyigemt.miraiboot.utils.CommandUtil;
import net.diyigemt.miraiboot.utils.EventHandlerManager;
import net.diyigemt.miraiboot.utils.ExceptionHandlerManager;
import net.diyigemt.miraiboot.utils.GlobalConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * <h2>注解扫描解析流程</h2>
 * @author diyigemt
 */
public class RegisterProcess {



    public static List<AutoInitItem> AnnotationScanner(List<Class<?>> classes, ConfigFile config){
        List<AutoInitItem> inits = new ArrayList<>();
        if (!classes.isEmpty()) {
            for (Class<?> clazz : classes) {
                if (clazz.isAnnotationPresent(AutoInit.class)) {
                    handleAutoInit(clazz, inits);
                }
                if (clazz.isAnnotationPresent(EventHandlerComponent.class)) {
                    handleEventHandler(clazz);
                }
                if (clazz.isAnnotationPresent(ExceptionHandlerComponent.class)) {
                    handleExceptionHandler(clazz);
                }
                if (clazz.isAnnotationPresent(MiraiBootComponent.class)) {
                    handleComponent(clazz);
                }
            }
        }
        return inits;
    }

    /**
     * 初始化被@AutoInit注释的类
     * @param res 暂存 等候排序
     * @param clazz 目标类
     */
    private static void handleAutoInit(Class<?> clazz, List<AutoInitItem> res) {
        String name = CommandUtil.getInstance().parseHandlerBaseName(clazz) + "." + "init";
        AutoInit annotation = clazz.getAnnotation(AutoInit.class);
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals("init") && method.canAccess(null)) {
                AutoInitItem item = new AutoInitItem(name, annotation.value(), method);
                res.add(item);
            }
        }
    }

    /**
     * 将被@EventHandlerComponent注册的类<br/>
     * 中被@EventHandler注册的方法加入EventhendlerManager中统一管理
     * @param clazz 被@EventHandlerComponent注册的类
     */
    private static void handleEventHandler(Class<?> clazz) {
        // 提取类私有的异常处理器
        List<ExceptionHandlerItem> handlers = null;
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(ExceptionHandler.class)) continue;
            if (handlers == null) handlers = new ArrayList<>();
            ExceptionHandler annotation = method.getAnnotation(ExceptionHandler.class);
            Class<? extends Exception> value = annotation.value();
            int priority = annotation.priority();
            String name = annotation.name();
            if (name.equals("")) name = CommandUtil.getInstance().parseHandlerBaseName(clazz, method);
            ExceptionHandlerItem item = new ExceptionHandlerItem(name, clazz, method, value, priority);
            handlers.add(item);
        }

        for (Method method : methods) {
            if (!method.isAnnotationPresent(EventHandler.class)) continue;
            EventHandler methodAnnotation = method.getAnnotation(EventHandler.class);
            // 注册其他事件Handler
            AtomicBoolean b = new AtomicBoolean(false);
            EventHandlerType[] types = methodAnnotation.type();
            for (EventHandlerType type : types) {
                if (type == EventHandlerType.OTHER_HANDLER) {
                    EventHandlerManager.getInstance().onOther("", clazz, method, handlers);
                    b.set(true);
                    break;
                }
            }
            // 如果注册为BotEventHandler 将不能被注册为消息事件Handler
            if (b.get()) continue;
            // 获取与指令对应的权限id
            EventHandlerComponent classAnnotation = clazz.getAnnotation(EventHandlerComponent.class);
            int permissionIndex = classAnnotation.value();
            if (method.isAnnotationPresent(CheckPermission.class)) {
                CheckPermission permission = method.getAnnotation(CheckPermission.class);
                permissionIndex = permission.functionId() == 0 ? permissionIndex : permission.functionId();
            }
            // 注册强制触发EventHandler
            if (methodAnnotation.isAny()) {
                EventHandlerManager.getInstance().onAny(clazz, method, handlers);
                String target = methodAnnotation.target();
                // 注册权限id
                String methodName = method.getName();
                String className = clazz.getCanonicalName();
                String s = target.equals("") ? className + "." + methodName : target;
                FunctionId.put(s, permissionIndex);
                continue;
            }
            String targetName = methodAnnotation.target();
            String start = methodAnnotation.start();
            if (targetName.equals("")) {
                targetName = method.getName();
            }
            if (start.equals("")) {
                Object o = GlobalConfig.getInstance().get(ConstantGlobal.DEFAULT_COMMAND_START);
                if (!o.toString().equals("")) targetName = o + targetName;
            } else {
                targetName = start + targetName;
                // 注册指令开头
                CommandUtil.getInstance().registerCommandStart(start);
            }

            FunctionId.put(targetName, permissionIndex);
            EventHandlerManager.getInstance().on(targetName, clazz, method, handlers);
        }
    }
    private static void handleExceptionHandler(Class<?> clazz) {
        // 不在事件处理器类中扫描异常处理器 防止重复注册
        if (clazz.isAnnotationPresent(EventHandlerComponent.class)) return;

        ExceptionHandlerComponent classAnnotation = clazz.getAnnotation(ExceptionHandlerComponent.class);
        int classPriority = classAnnotation.value();
        for (Method method : clazz.getMethods()) {
            if (!method.isAnnotationPresent(ExceptionHandler.class)) continue;
            ExceptionHandler annotation = method.getAnnotation(ExceptionHandler.class);
            if (!method.isAnnotationPresent(ExceptionHandler.class)) continue;
            // 检查返回值类型
            Class<?> returnType = method.getReturnType();
            if (!(returnType == void.class || returnType == boolean.class)) continue;
            int priority = annotation.priority();
            if (priority == 0 && classPriority != 0) priority = classPriority;
            Class<? extends Exception> value = annotation.value();
            String name = annotation.name();
            if (name.equals("")) name = CommandUtil.getInstance().parseHandlerBaseName(clazz, method);
            ExceptionHandlerItem item = new ExceptionHandlerItem(name, clazz, method, value, priority);
            ExceptionHandlerManager.getInstance().on(item);
        }
    }
    private static void handleComponent(Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            if (!method.isAnnotationPresent(ConsoleCommand.class)) continue;
            ConsoleCommand annotation = method.getAnnotation(ConsoleCommand.class);
            String value = annotation.value();
            MiraiBootConsole.getInstance().on(value, clazz, method);
        }
    }
}
