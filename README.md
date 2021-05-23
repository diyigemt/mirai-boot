## 声明

<h3>一切开发旨在学习，请勿用于非法用途</h3>

- miraiboot 是一款免费且开放源代码的软件，仅供学习和娱乐用途使用。
- miraiboot 不会通过任何方式强制收取费用，或对使用者提出物质条件。
- miraiboot 由整个开源社区维护，并不是属于某个个体的作品，所有贡献者都享有其作品的著作权。

## 许可证

详见 https://github.com/diyigemt/mirai-boot/blob/master/LICENSE

miraiboot 继承 [Mirai](https://github.com/mamoe/mirai) 使用 AGPLv3 协议开源。为了整个社区的良性发展，我们强烈建议您做到以下几点：

- 间接接触到 miraiboot 的软件使用 AGPLv3 开源
- **不鼓励，不支持一切商业使用**

请注意，由于种种原因，开发者可能在任何时间**停止更新**或**删除项目**。

### 衍生软件需声明引用

- 若引用 miraiboot 发布的软件包而不修改 miraiboot，则衍生项目需在描述的任意部位提及使用 miraiboot。
- 若修改 miraiboot 源代码再发布，或参考 miraiboot 内部实现发布另一个项目，则衍生项目必须在文章首部或 'miraiboot' 相关内容首次出现的位置明确声明来源于本仓库 ([mirai-boot](https://github.com/diyigemt/mirai-boot))。
- 不得扭曲或隐藏免费且开源的事实。

---
## Statement

<h3>All development is for learning, please do not use it for illegal purposes</h3>

- miraiboot is a free and open source software for learning and entertainment purposes only.
- miraiboot will not compulsorily charge fees or impose material conditions on users in any way.
- miraiboot is maintained by the entire open source community and is not a work belonging to an individual. All contributors enjoy the copyright of their work.
## License

See https://github.com/diyigemt/mirai-boot/blob/master/LICENSE for details

miraiboot inherits [Mirai](https://github.com/mamoe/mirai) Open source using AGPLv3 protocol. For the healthy development of the entire community, we strongly recommend that you do the following:

- Software indirectly exposed to miraiboot uses AGPLv3 open source
- **Does not encourage and does not support all commercial use**

Please note that for various reasons, developers may **stop updating** or **deleting** projects at any time.

### Derivative software needs to declare and quote

- If you quote the package released by miraiboot without modifying miraiboot, the derivative project needs to mention miraiboot in any part of the description.
- If the miraiboot source code is modified and then released, or another project is released by referring to miraiboot's internal implementation, the derivative project must be clearly stated in the first part of the article or at the location where'miraiboot'-related content first appears from this repository ([mirai-boot](https://github.com/diyigemt/mirai-boot)).
- The fact that it is free and open source must not be distorted or hidden.

---

## 介绍

miraiboot是是对mirai框架的简单Java封装。

目的是为了让Java开发者更方便地开发基于指令响应的机器人。

## 特点

1. 不用关心mirai-core的代码

   miraiboot提供了一系列方便的工具类对mirai-core的核心功能依赖进行封装，如消息回复、语音、图片等本地文件的发送等，对于简单的qq机器人开发，Java开发者不需要在去接触kotlin代码，更适合于Java初级开发者。

2. 自带简单的消息过滤器和权限管理模块

   miraiboot提供了一系列的工具，可以方便地对消息事件进行过滤和权限管理。

   权限管理基于SQLite且已经进行了封装，开发者无需考虑实现。

3. 注解驱动开发

   miraiboot所有的事件和异常处理都通过注解完成，开发者只需要对处理方法加上对应的注解，其余的都交由miraiboot进行管理，让开发者专注于功能的实现。

## 安装教程

1. 在maven中添加依赖



你也可以去 [target目录](https://gitee.com/fanwen-magician/mirai-java-easy/blob/master/target)下载jar包并导入项目。但不推荐这样做。
**注：第一次miraiboot需要引入mirai-login-solver-selenium这个依赖以完成新设备的滑动验证**



## 快速上手

创建一个主类

```java
import org.miraiboot.annotation.MiraiBootApplication;
import org.miraiboot.autoconfig.MiraiApplication;

@MiraiBootApplication
public class Main {
	public static void main(String[] args) {
		MiraiApplication.run(Main.class);
	}
}
```

在主类包下任意一个包或者直接在主类旁边创建一个新的类

```java
@EventHandlerComponent
public class Test {
	@EventHandler(target = "复读")
	public void test(MessageEventPack eventPack, PreProcessorData data) {
		eventPack.reply(eventPack.getMessage());
	}
}
```

好了 现在你拥有了一个复读机器人 他会复读任何带有"复读"这两个字的消息

当然 你也可以将target留空(默认) 他就真的会复读任何东西了23333

在上面用到的三个注解可以将在下面详细解释 当然也可以去看注释(反正下面的东西也是从注释那边复制过来的)

**注意 第一次运行会在项目目录创建config cache和data三个文件夹**

config：保存miraiboot的配置文件和bot的device.json文件

cache：保存mirai-core的文件

data：保存miraiboot的资源文件 放在其中的文件可以很方便地通过工具类访问

关于事件过滤：

```java
@EventHandlerComponent
public class Test {
	@EventHandler(target = "复读")
    @MessageFilter(accounts = "1231231231")
	public void test(MessageEventPack eventPack, PreProcessorData data) {
		eventPack.reply(eventPack.getMessage());
	}
}
```

现在这个方法只会处理来自于qq号1231231231的消息事件了

关于权限管理

```java
@EventHandlerComponent
public class Test {
	@EventHandler(target = "复读")
	@CheckPermission(blocks = "123123123", isAdminOnly = true)
	public void test(MessageEventPack eventPack, PreProcessorData data) {
		eventPack.reply(eventPack.getMessage());
	}
}
```

现在这个方法只会处理群管理的消息事件，并且不允许qq号为123123123的管理使用

关于事件预处理

```java
@EventHandlerComponent
public class Test {
	@EventHandler(target = "复读")
	@MessagePreProcessor(filterType = MessagePreProcessorMessageType.At)
	public void test(MessageEventPack eventPack, PreProcessorData data) {
		eventPack.reply(eventPack.getMessage());
	}
}
```

现在 PreProcessorData的classified属性中会存有该消息事件中的所有At消息

至于用来干什么  比如说过滤图片 过滤出来的list可以用来判断消息中有没有图片 做个搜图功能之类的

当然 上面那三个@注解是可以叠起来用的

关于ExceptionCatch

来 我们新建一个类

```java
@EventHandlerComponent
public class ExceptionHandler {
	@ExceptionHandler(targets = IllegalArgumentException.class, priority = 1)
	public void test(Throwable e){
        BotManager.getInstance().get(12312312L).getGroup(12313123L).sendMessage("error" + 		  e.getMessage());
	}
}
```

现在它会处理所有IllegalArgumentException 

当然这种方法目前还不完善 比如需要用户自己获得bot自己获取群自己回消息(这不是完全没做吗23333)

targets可以接受一个数组 priority指代执行优先级 你可以为一个异常指定多个handler  同时方法可以返回一个boolean 当值为true是阻止低优先级的handler的触发

关于动态权限管理  先看注释吧 

关于配置文件 生成的文件里每一行都有注释

关于其他demo  可以去function包下看看

# 详细开发文档

## 系统启动

创建一个主类，并调用MiraiApplication的静态run方法。

```java
import org.miraiboot.annotation.MiraiBootApplication;
import org.miraiboot.autoconfig.MiraiApplication;

@MiraiBootApplication
public class Main {
	public static void main(String[] args) {
		MiraiApplication.run(Main.class);
	}
}
```

系统第一次运行会在项目目录创建config cache和data三个文件夹

config：保存miraiboot的配置文件和bot的device.json文件

cache：保存mirai-core的文件

data：保存miraiboot的资源文件 放在其中的文件可以很方便地通过工具类访问

其中在config文件夹下的application.yml文件是系统的配置文件，各种设置项请参考文件注释

## 事件处理

miraiboot的事件可以分为两类：消息事件(群聊 好友会话和通过群发起的临时会话)和其他事件。它们都可以通过以下方式处理

### 消息事件处理

通过对类加上`@EventHandlerComponent`的注解，并对其中的方法加上`@EventHandler`的注解，可以将一个方法注册为消息事件处理器。例如：

```java
@EventHandlerComponent
public class Test {
	@EventHandler(target = "复读")
	public void test(MessageEventPack eventPack, PreProcessorData data) {
		eventPack.reply(eventPack.getMessage());
	}
}
```

这个消息事件处理器的功能是对所有包含纯文本消息"复读"的内容进行复读。

方法之多有**2**个参数，第一个是`MessageEventPack` 包含了对消息事件本体的简单封装，第二个参数是`PreProcessorData` 是对消息事件的一些预处理内容，**多余**的参数将会在执行时传入null。

以上的示例使用了4个miraiboot的类，下面将会分别详细介绍它们的参数和用法

#### @EventHandlerComponent

将该类标注为事件处理器类，启动时将会在该类下将标注了`@EventHandler`注解的方法注册为消息事件处理器

| 类型 | 属性名 | 默认值 | 说明                                               |
| ---- | ------ | ------ | -------------------------------------------------- |
| int  | value  | 0      | 该类中所有被标注为消息处理器在权限表中的默认权限id |

#### @EventHandler

将受到注解的方法标注为消息事件处理器

| 类型               | 属性名 | 默认值              | 说明                                                       |
| ------------------ | ------ | ------------------- | ---------------------------------------------------------- |
| String             | target | ""                  | 匹配的指令名，只有消息中包含该指令名才会触发消息事件处理器 |
| String             | start  | ""                  | 指令开头                                                   |
| String(正则)       | split  | "\\s+"              | 用于分离参数的正则表达式                                   |
| EventHandlerType[] | type   | MESSAGE_HANDLER_ALL | 指定需要处理的消息类型                                     |
| boolean            | isAny  | false               | 为true时忽略type和target，即处理所有消息事件               |

##### `target`：

miriaboot是基于指令响应的机器人，**期望**所有事件处理器都需要有一个对应的指令才会触发，当target留空时，所有消息事件**均会**触发这个消息事件处理器。仅支持**纯中文|英文数字混合**两种类型。

##### `start`：

指令开头，与指令名`target`一起组成一个**完整**的指令，同时也是指令解析的**整体**。例如：

```java
@EventHandler(target = "help", start = "/")
public void test1() {}
@EventHandler(target = "help", start = ".")
public void test2() {}
```

以上两个注解将会注册两个指令："/help"和".help"，在消息中发送"/help"和".help"将会分别触发test1()和test2()方法。

在配置文件中的字段`DEFAULT_COMMAND_START`可以注册默认的指令开头，在注解的`start`内容为空时会取配置文件中的值。

##### `split`：

将指令与其后的参数分割开的*正则表达式*，默认值为"\\s+"也就是不定长空格。例如：

```JAVA
@EventHandler(target = "help", start = "/")
public void test1() {}
```

对于输入"一些乱七八糟的东西　/help arg1 arg2   arg3"，将会解析出全指令："/help arg1 arg2   arg3"，指令名："/help"，参数列表：[arg1, arg2, arg3]

##### `type`：

指定消息事件处理器需要处理的消息类型，当消息事件类型符合时才会触发消息事件处理器，具体如下：

| 值                     | 说明               |
| ---------------------- | ------------------ |
| MESSAGE_HANDLER_FRIEND | 处理好友消息事件   |
| MESSAGE_HANDLER_TEMP   | 处理群临时会话事件 |
| MESSAGE_HANDLER_GROUP  | 处理群聊消息事件   |
| MESSAGE_HANDLER_ALL    | 处理以上三种事件   |
| OTHER_HANDLER          | 处理其他类型的事件 |

##### `isAny`：

当其值为true时，注册一个强制触发的消息事件处理器，忽略target，split，start，type的参数设置，处理所有类型的消息事件。

#### MessageEventPack

`MessageEventPack`是对消息事件的简单封装，提供了一系列方法对消息事件进行操作，大体可分为三类：获取消息事件信息、对消息事件进行回复和上下文监听器注册

##### 获取消息事件信息

| 方法原型                                                     | 说明                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| isGroup(): boolean                                           | 判断消息事件是否是群消息事件                                 |
| isFriend(): boolean                                          | 判断消息事件是否是好友消息事件                               |
| isGroupTmp(): boolean                                        | 判断消息事件是否是群临时会话消息事件                         |
| getSenderPermission():MemberPermission                       | 获取群消息发送者的权限，若不是群消息事件返回MemberPermission.MEMBER |
| getSenderNick():String                                       | 获取发送人名称. 由群员发送时为群员名片, 由好友发送时为好友昵称 |
| getBotId():long                                              | 获取收到该消息的bot的qq号                                    |
| getTime():int                                                | 获取收到消息的时间戳                                         |
| getSource():Incoming                                         | 获取消息的来源信息 具体详细信息:[详情](https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/MessageSource.kt) |
| getSenderName(): String                                      | 获取发送者的名片  同getSenderNick()                          |
| getSender(): User                                            | 获取发送者                                                   |
| getBot():Bot                                                 | 获取收到该事件的bot                                          |
| getSubject():Contact                                         | 获取事件主体                                                 |
| getMessage():MessageChain                                    | 获取消息链                                                   |
| <T extends SingleMessage> getMessageByType(Class<T>): List<T> | 根据提供的消息类型获取过滤后的消息链                         |
| getGroup():Group                                             | 获取当前消息来源的群对象，如果不是群消息事件返回null         |
| getGroup(long):Group                                         | 获取群号对应的一个群，如果机器人不在群内返回null             |
| getFriend(long)： Friend                                     | 获取qq号对应的好友，如果不是好友返回null                     |
| getSenderId():long                                           | 获取消息事件发送者的qq号                                     |
| getEventType():EventType                                     | 获取消息事件类型                                             |
| getEvent(): MessageEvent                                     | 获取消息事件本身                                             |

##### 对消息事件进行回复

| 方法原型                                  | 说明                                                         |
| ----------------------------------------- | ------------------------------------------------------------ |
| reply(String...):void                     | 将所有传入的字符串拼接成一条纯文本消息回复发送者，如果是群消息将会at发送者 |
| reply(SingleMessage...):void              | 将所有传入的所有消息拼接成一条消息回复发送者，如果是群消息将会at发送者 |
| reply(MessageChain):void                  | 将所有传入的消息链回复发送者，如果是群消息将会at发送者       |
|                                           |                                                              |
| replyNotAt(String...):void                | 将所有传入的字符串拼接成一条纯文本消息回复发送者，如果是群消息将不会at发送者 |
| replyNotAt(SingleMessage...):void         | 将所有传入的所有消息拼接成一条消息回复发送者，如果是群消息将不会at发送者 |
| replyNotAt(MessageChain):void             | 将所有传入的消息链回复发送者，如果是群消息将不会at发送者     |
|                                           |                                                              |
| sendGroupMessage(long, String...):boolean | 将所有传入的字符串拼接成一条纯文本消息并向指定的群发送       |

