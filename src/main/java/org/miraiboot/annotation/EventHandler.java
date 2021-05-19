package org.miraiboot.annotation;

import org.miraiboot.constant.EventHandlerType;

import java.lang.annotation.*;

import static org.miraiboot.constant.EventHandlerType.MESSAGE_HANDLER_ALL;


/**
 * <h2>将受到注解的方法注册为事件handler</h2>
 * <strong>注意, 目前最多支持2参数的方法 多余的参数将会传入null<strong/>
 * <strong>受到注解的方法必须为public的</strong>
 * <p>
 *   例如
 * <pre>
 * {@code
 * 在群消息中发送任何一条含有aaa的消息就会被触发
 * @EventHandler(target = "aaa")
 * public void test1() { your code }
 * 在群消息中发送任何一条含有/aaa的消息就会被触发
 * @EventHandler(start = "/", target = "aaa")
 * public void test1(MessageEvent event) { your code }
 * 任何消息均会触发该事件处理器
 * !!此时消息中所有纯文本内容均会作为参数被解析后放入data.args中!
 * @EventHandler(isAny = true)
 * public void test1(MessageEvent event, PreProcessData data) { your code }
 * }
 * </pre>
 * </p>
 * 关于PreProcessData的内容, 请参考{@link org.miraiboot.entity.PreProcessorData}
 * @author diyigemt
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventHandler {
  /**
   * 是否为常驻消息处理器<br/>
   * 设置为true时<strong>无论如何</strong>该消息处理器均会触发
   * @since 1.0.0
   */
  boolean isAny() default false;
  /**
   * 匹配的指令开头 为空表示忽略<br/>
   * 如:@MessageEventFilter(start = "/")
   * 表示将从 '/'之后的内容获取指令名和参数<br/>
   * 如:消息 '一些乱七八糟的内容 /help asd'<br/>
   * 将会获取到指令 help 和参数 asd
   * 当配置文件中存在DEFAULT_COMMAND_START非空配置项且start值为空时 将使用配置项代替
   * @since 1.0.0
   */
  String start() default "";
  /**
   * <h3>参数分隔符 用于获取指令之后的文本参数</h3>
   * 值为正则表达式 默认使用'\\s+' 即以至少1个空格为分割<br/>
   * 如:@EventHandler(start = "/", split = "\\s+")<br/>
   * 处理消息 '一些乱七八糟的内容 /help asd    ssd'<br/>
   * 将会得到 指令 help 和参数 asd与ssd
   * @since 1.0.0
   */
  String split() default "\\s+";
  /**
   * 触发handler的指令 为空时取受到注解的方法名
   * @since 1.0.0
   */
  String target() default "";
  /**
   * 处理类型 群消息或好友消息或者全部<br/>
   * 更多消息类型在做了
   * @since 1.0.0
   * @see EventHandlerType
   */
  EventHandlerType[] type() default MESSAGE_HANDLER_ALL;
}
