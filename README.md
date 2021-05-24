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
@ExceptionHandlerComponent
public class ExceptionHandler {
	@ExceptionHandler(targets = IllegalArgumentException.class, priority = 1)
	public void test(Throwable e){
        BotManager.getInstance().get(12312312L).getGroup(12313123L).sendMessage("error" + e.getMessage());
	}
}
```

现在它会处理所有IllegalArgumentException 

当然这种方法目前还不完善 比如需要用户自己获得bot自己获取群自己回消息(这不是完全没做吗23333)

targets可以接受一个数组，priority指代执行优先级。

你可以为一个异常指定多个handler，同时方法可以返回一个boolean，当值为true时阻止低优先级的handler的触发

关于动态权限管理  先看注释吧 

关于配置文件 生成的文件里每一行都有注释

关于其他demo  可以去function包下看看

# 详细开发文档

## 目录

[TOC]



## 系统启动

创建一个主类，并调用MiraiApplication的静态run方法。

```java
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

方法至多有**2**个参数，第一个是`MessageEventPack` ，其包含了对消息事件本体的简单封装，第二个参数是`PreProcessorData` ，是对消息事件的一些预处理内容，**多余**的参数将会在执行时传入null。

以上的示例使用了4个miraiboot的类，下面将会分别详细介绍它们的参数和用法

#### @EventHandlerComponent

将该类标注为事件处理器类，启动时将会在该类下将标注了`@EventHandler`注解的方法注册为消息事件处理器

| 类型 | 属性名 | 默认值 | 说明                                               |
| ---- | ------ | ------ | -------------------------------------------------- |
| int  | value  | 0      | 该类中所有被标注为消息处理器在权限表中的默认权限id |

#### @EventHandler

将受到注解的方法指定为消息事件处理器

| 类型               | 属性名 | 默认值              | 说明                                                       |
| ------------------ | ------ | ------------------- | ---------------------------------------------------------- |
| String             | target | ""                  | 匹配的指令名，只有消息中包含该指令名才会触发消息事件处理器 |
| String             | start  | ""                  | 指令开头                                                   |
| String(正则)       | split  | "\\s+"              | 用于分离参数的正则表达式                                   |
| EventHandlerType[] | type   | MESSAGE_HANDLER_ALL | 指定需要处理的消息类型                                     |
| boolean            | isAny  | false               | 为true时忽略type和target，即处理所有消息事件               |

##### `target`：

miriaboot是基于指令响应的机器人，**期望**所有事件处理器都需要有一个对应的指令才会触发，指令仅支持**纯中文|英文数字混合**两种类型。

当target留空时，所有消息事件**均会**触发这个消息事件处理器。

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

将指令与其后的参数分割开的*正则表达式*，默认值为"\\s+"，也就是不定长空格。例如：

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

| 方法原型                                              | 说明                                                         |
| ----------------------------------------------------- | ------------------------------------------------------------ |
| reply(String...):void                                 | 将所有传入的字符串拼接成一条纯文本消息回复发送者，如果是群消息将会at发送者 |
| reply(SingleMessage...):void                          | 将所有传入的所有消息拼接成一条消息回复发送者，如果是群消息将会at发送者 |
| reply(MessageChain):void                              | 将所有传入的消息链回复发送者，如果是群消息将会at发送者       |
| reply(EnhancedMessageChain):void                      | 将所有传入的消息链列表逐一回复发送者，如果是群消息将会at发送者<br/>为啥要用list呢，因为发送语音和文件的时候 不能和其他类型的消息一起发送，所以要分开多条消息来发 |
| replyNotAt(String...):void                            | 将所有传入的字符串拼接成一条纯文本消息回复发送者，如果是群消息将不会at发送者 |
| replyNotAt(SingleMessage...):void                     | 将所有传入的所有消息拼接成一条消息回复发送者，如果是群消息将不会at发送者 |
| replyNotAt(MessageChain):void                         | 将所有传入的消息链回复发送者，如果是群消息将不会at发送者     |
| replyNotAt(EnhancedMessageChain):void                 | 将所有传入的消息链列表回复发送者，如果是群消息将会at发送者   |
| sendGroupMessage(long, String...):boolean             | 将所有传入的字符串拼接成一条纯文本消息并向指定的群发送，群不存在时返回false |
| sendGroupMessage(long, SingleMessage...):boolean      | 将所有传入的所有消息拼接成一条消息并向指定的群发送，群不存在时返回false |
| sendGroupMessage(long, MessageChain):boolean          | 将所有传入的消息链向指定的群发送，群不存在时返回false        |
| sendGroupMessage(long, EnhancedMessageChain):boolean  | 将所有传入的消息链列表逐一向指定的群发送，群不存在时返回false |
| sendTempMessage(long, long, String...):boolean        | 将所有传入的字符串拼接成一条纯文本消息并向指定的群中指定的群员发送临时会话，群或群友不存在时返回false |
| sendTempMessage(long, long, SingleMessage...):boolean | 将所有传入的所有消息拼接成一条消息并向指定的群中指定的群员发送临时会话，群或群友不存在时返回false |
| sendTempMessage(long, long, MessageChain):boolean     | 将所有传入的消息链向指定的群中指定的群员发送临时会话，群或群友不存在时返回false |
| sendFriendMessage(long, String...):boolean            | 将所有传入的字符串拼接成一条纯文本消息并向指定的好友发送，好友不存在时返回false |
| sendFriendMessage(long, SingleMessage...):boolean     | 将所有传入的所有消息拼接成一条消息并向指定的好友发送，好友不存在时返回false |
| sendFriendMessage(long, MessageChain):boolean         | 将所有传入的消息链向指定的好友发送，好友不存在时返回false    |
| sendFriendMessage(long, EnhancedMessageChain):boolean | 将所有传入的消息链列表逐一向指定的好友发送，好友不存在时返回false |

##### 上下文监听器注册

上下文监听器`EventHandlerNext`是一个抽象类，任何继承并实现其中`onNext`方法的类均可以被注册成为上下文监听器

```java
public abstract class EventHandlerNext {
  /**
   * <h2>上下文监听器方法</h2>
   * 该方法可以使用@MessageEventFilter注解来过滤信息
   * @param eventPack 事件本身
   * @param data 预处理器
   * @return 返回是否继续监听事件 ListeningStatus.LISTENING表示继续监听 STOPPED表示停止监听
   */
  public abstract ListeningStatus onNext(MessageEventPack eventPack, PreProcessorData data);

  /**
   * <h2>监听器销毁时调用</h2>
   * @param eventPack 最后一次触发监听器的事件Event
   * @param data 最后一次触发监听器的PreProcessorData
   */
  public void onDestroy(MessageEventPack eventPack, PreProcessorData data) { }

  /**
   * <h2>监听器超时时调用</h2>
   * @param eventPack 最后一次触发监听器的事件Event
   * @param data 最后一次触发监听器的PreProcessorData
   */
  public void onTimeOut(MessageEventPack eventPack, PreProcessorData data) { }

  /**
   * <h2>监听器触发次数耗尽时调用</h2>
   * @param eventPack 最后一次触发监听器的事件Event
   * @param data 最后一次触发监听器的PreProcessorData
   */
  public void onTriggerOut(MessageEventPack eventPack, PreProcessorData data) { }
```

| 方法原型                                                     | 说明                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| <T extends EventHandlerNext> onNext(T):void                  | 注册一个上下文监听器，使用全局配置的超时时间，监听本事件的发送者 |
| <T extends EventHandlerNext> onNext(T, long):void            | 注册一个上下文监听器，设置超时时间为long，监听本事件的发送者 |
| <T extends EventHandlerNext> onNext(T, int):void             | 注册一个上下文监听器，使用全局配置的超时时间，监听本事件的发送者，并至多触发int次 |
| <T extends EventHandlerNext> onNext(T, long, int):void       | 注册一个上下文监听器，设置超时时间为long，监听本事件的发送者，并至多触发int次 |
| <T extends EventHandlerNext> onNext(long, T, long, int):void | 注册一个上下文监听器，设置超时时间为long，监听第一个long对应的群友，并至多触发int次 |
| <T extends EventHandlerNext> onNextNow(T, PreProcessorData):void | 注册一个上下文监听器，使用全局配置的超时时间，监听本事件的发送者，并立即开始倒计时 |
| <T extends EventHandlerNext> onNextNow(T, PreProcessorData, long, int):void | 注册一个上下文监听器，设置超时时间为long，监听本事件的发送者，至多触发int次，并立即开始倒计时 |
| <T extends EventHandlerNext> onNextNow(long, T, PreProcessorData, long, int):void | 注册一个上下文监听器，设置超时时间为long，监听第一个long对应的群友，至多触发int次，并立即开始倒计时 |

#### PreProcessorData

`PreProcessorData`是对消息事件的处理结果，包含了解析出的指令、参数、纯文本和@MessagePreProcessor的预处理结果

| 属性         | 类型                | 说明                                         |
| ------------ | ------------------- | -------------------------------------------- |
| commandText  | String              | 匹配到的包含指令开头所有内容                 |
| command      | String              | 匹配到的指令                                 |
| args         | List<String>        | 匹配到的参数                                 |
| text         | String              | 消息纯文本                                   |
| classified   | List<SingleMessage> | 过滤出的消息内容                             |
| triggerCount | int                 | 上下文触发剩余次数(仅在上下文监听事件中有用) |

### 其他事件处理

通过对类加上`@EventHandlerComponent`的注解，并对其中的方法加上`@EventHandler(type = EventHandlerType.OTHER_HANDLER)`的注解，可以将一个方法注册为非消息事件的事件处理器。例如：

```java
@EventHandlerComponent
public class Test {
   @EventHandler(type = EventHandlerType.OTHER_HANDLER)
   public void test(BotEventPack eventPack) {
       BotEvent event = eventPack.getEvent();
       if (event instanceof MemberCardChangeEvent) {
       		String aNew = ((MemberCardChangeEvent) event).getNew();
       		event.getBot().getGroup(1231231312L).sendMessage("change to: " + aNew);
       }
   }
}
```

这个事件处理器的功能是监听群友的名片变化并发送到群中。

被注册的方法至多有**1**个参数`BotEventPack`里面目前只有一个属性，即事件本身，**多余** 的参数将会在执行时传入null。

### 消息事件预处理

#### @MessageFilter

消息事件过滤器，在所有规则通过时EventHandler才会被触发

| 类型                   | 属性名    | 默认值                      | 说明                                              |
| ---------------------- | --------- | --------------------------- | ------------------------------------------------- |
| String                 | value     | ""                          | 匹配的内容，为空表示忽略                          |
| MessageFilterMatchType | matchType | MessageFilterMatchType.NULL | 关键词的匹配类型，为NULL表示忽略                  |
| String[]               | accounts  | {}                          | 若消息发送着不在列表内则不做响应，为空表示忽略    |
| String[]               | groups    | {}                          | 若消息发送着不在列表内则不做响应，为空表示忽略    |
| String[]               | bots      | {}                          | 若消息接受的bot不在列表内则不做响应，为空表示忽略 |
| boolean                | isAt      | false                       | 是否当bot被at时才触发                             |
| boolean                | isAtAll   | false                       | 是否at全体时才触发                                |
| boolean                | isAtAny   | false                       | 是否有人被at时才触发，不一定是bot被at             |

##### `value`：

需要匹配的内容，为空表示忽略

匹配将会对消息内容的**所有纯文本**进行匹配

##### `MessageFilterMatchType`：

消息匹配类型

| 值                 | 说明                 |
| ------------------ | -------------------- |
| NULL               | 忽略                 |
| EQUALS             | 相同匹配             |
| EQUALS_IGNORE_CASE | 忽略大小写的相同匹配 |
| CONTAINS           | 包含匹配             |
| STARTS_WITH        | 开头匹配             |
| ENDS_WITH          | 结尾匹配             |
| REGEX_MATCHES      | 正则全文匹配         |
| REGEX_FIND         | 正则查找匹配         |

#### @MessagePreProcessor

消息事件预处理器，对收到的消息事件进行预处理

| 类型                             | 属性名        | 默认值 | 说明                                                        |
| -------------------------------- | ------------- | ------ | ----------------------------------------------------------- |
| boolean                          | textProcessor | false  | 将所有纯文本消息提取出来保存在PreProcessorData.text         |
| MessagePreProcessorMessageType[] | filterType    | {}     | 将对应类型的消息提取出来保存在PreProcessorData.classified中 |

##### `textProcessor`：

其实就目前来说此项会一直有效

##### `filterType`：

过滤类型，对应mirai-core的消息类型

| 值                   | 说明                           |
| -------------------- | ------------------------------ |
| PlainText            | 纯文本                         |
| Image                | 图片                           |
| At                   | 艾特(@)人的消息                |
| AtAll                | 艾特(@)全体的消息              |
| Face                 | 自带的表情栏的表情             |
| FlashImage           | 闪照                           |
| PokeMessage          | 戳一戳                         |
| VipFace              | vip才能用的表情                |
| LightApp             | 快应用                         |
| Voice                | 语音                           |
| MarketFace           | 表情市场中的表情               |
| ForwardMessage       | 合并转发的消息                 |
| SimpleServiceMessage | 另一种快应用的消息类型，不常用 |
| MusicShare           | 音乐分享                       |
| Dice                 | 骰子消息                       |
| FileMessage          | 文件消息                       |

## 消息的构造和发送

#### 消息链概念介绍

消息链（MessageChain）是指一种由多个类型元素组成的链表结构。任意一种继承Message类的元素均可成为链表中的一个节点，例如文字PlainText、语音Voice、图片Image、文件FileMessage等等，统称为SingleMessage。多个SingleMessage连接到一起，即为消息链。下图即是一条图文消息的消息链构成。

![](https://lychee.diyigemt.net/uploads/big/312aab6dde35cabb196d83317f74691c.jpg)

值得注意的是，SingleMessage并不一定是实际显示中的一条消息，它指的是一条消息中的一个元素。如果该消息由多个元素构成（例如图文消息），则实际显示中一条消息是MessageChain。而且，对于语音元素和文件元素，这里将以上两种元素成为特殊元素。由于QQ自身对特殊元素显示规则等原因，每条消息链只能接受一个特殊元素。一旦插入特殊元素，之前的图文元素会被忽略，如果插入多个特殊元素，实际显示时只会显示消息链中最后一个特殊元素。



#### MessageChain类型

MessageChain类型是Mirai-core中自带的默认消息链类型，用于存储纯文本消息和图文消息的构成。详情请看Mirai-core官方文档。下面列举一些常用的方法：

| 返回类型     | 方法                    | 参数                                | 说明                   |
| ------------ | ----------------------- | ----------------------------------- | ---------------------- |
| MessageChain | .plus()                 | Char, SingleMessage, MessageChain等 | 向消息链中添加消息元素 |
| String       | .serializeToMiraiCode() | 无                                  | 将消息转换为Mirai格式  |



#### EnhancedMessageChain类型

在[消息链概念介绍](消息链概念介绍)中我们指出了MessageChain关于特殊元素的不足之处，而在MiraiBoot中，我们对此进行了一些改进和封装，新增了一个类型：EnhancedMessageChain（加强消息链）。其本质为List<MessageChain>，用于支持特殊消息混合插入。同时加入URL和本地路径识别，可以自动联网获取特殊元素素材进行发送，无需自行下载。



相比MessageChain，EnhancedMessageChain可以将图文信息和群文件以及语音元素混合插入，打破了MessageChain对于特殊元素不能添加多个的限制。同时EnhancedMessageChain也支持连接使用MessageChainBuilder构造完成的结果。

EnhancedMessageChain类型拥有以下方法：

具体请阅读[Mirai文档](https://github.com/mamoe/mirai/blob/dev/docs/Messages.md#%E6%9E%84%E9%80%A0%E6%B6%88%E6%81%AF%E9%93%BE)

| 返回类型 | 方法      | 参数                 | 说明                     |
| -------- | --------- | -------------------- | ------------------------ |
| void     | .append() | MessageChain         | 加入MessageChain         |
| void     | .append() | EnhancedMessageChain | 加入EnhancedMessageChain |



同时，EnhancedMessageChain类型拥有迭代器，支持Foreach循环

```java
EnhancedMessageChain chains = new ImageMessageChainBulider(eventPack)
    .add("1")
    .add("2")
    .build();

for(MessageChain chain : chains){
    chain.plus("A");
}
```



#### 消息构造器

在Mirai-Core中，已经拥有一种默认构造器如下:

| 构造器              | 参数 | 返回类型     |
| ------------------- | ---- | ------------ |
| MessageChainBuilder | 无   | MessageChain |

该构造器常用的方法：

具体请阅读[Mirai开发文档](https://github.com/mamoe/mirai/blob/dev/docs/Messages.md#%E6%9E%84%E9%80%A0%E6%B6%88%E6%81%AF%E9%93%BE)

| 返回类型 | 方法      | 参数                                | 说明               |
| -------- | --------- | ----------------------------------- | ------------------ |
| void     | .append() | Char, SingleMessage, MessageChain等 | 向消息链中添加元素 |
| void     | .build()  | 无                                  | 构造消息链         |

使用样例：

```java
MessageChain chain = new MessageChainBuilder()
    .plus("Hello")
    .build();
```

构造完成后发送：

```java
eventPack.reply(chain);
```



在MiraiBoot中，新增了三种消息构造器为EnhancedMessageChain服务。其使用方法和原版MessageChainBuilder基本保持一致。但MessageChainBuilder并不能为EnhancedMessageChain构造消息。介绍如下。

| 构造器              | 参数                                        | 说明           | 返回类型             |
| ------------------- | ------------------------------------------- | -------------- | -------------------- |
| ImageMessageBuilder | EventPack（必填）, HttpProperties（可不填） | 图片消息构造器 | EnhancedMessageChain |
| VoiceMessageBuilder | EventPack（必填）, HttpProperties（可不填） | 语音消息构造器 | EnhancedMessageChain |
| FileMessageBuilder  | EventPack（必填）, HttpProperties（可不填） | 文件消息构造器 | EnhancedMessageChain |

使用样例：

```java
EnhancedMessageChain chains = new ImageMessageChainBulider(eventPack).build();//一般实例化

HttpProperties properties = new HttpProperties();
properties.setRequestProperties("Connection", "keep-alive");

EnhancedMessageChain chains = new ImageMessageChainBulider(eventPack, httpProperties).build();//对于HTTP有特殊需求的实例化
```

每一种Builder拥有相同的方法：

| 返回类型             | 方法    | 参数                 | 说明                                              |
| -------------------- | ------- | -------------------- | ------------------------------------------------- |
| xxxMessageBuilder    | add()   | MessageChain         | 将之前构造好的消息链依次接入尾部                  |
| xxxMessageBuilder    | add()   | EnhancedMessageChain | 将之前构造好的消息链依次接入尾部                  |
| xxxMessageBuilder    | add()   | String...            | 自动识别文字消息、URL、本机路径，后两者会自动解析 |
| xxxMessageBuilder    | add()   | File                 | 将打开的File类传入解析                            |
| EnhancedMessageChain | build() | 无                   | 构造消息但不发送                                  |
| EnhancedMessageChain | send()  | 无                   | 构造消息并发送                                    |



注：

1. Miraiboot会等到消息链中所有素材都加载完成并上传至服务器才会进行发送操作。所以URL的连接质量直接影响您发送消息的延迟，请尽量使用连接质量好的URL或使用HTTPProperties进行Host等设置优化。实在不行可以考虑事先下载好素材使用本地路径或File类打开传入。
2. 请不要在一条加强消息链上添加过多语音、群文件等特殊元素，可能会因为消息接收方带宽限制和QQ消息同步规则使得接收方消息乱序，得不偿失。
3. 对于没有传入HTTPProperties的Builder实例化，Miraiboot将使用全局默认的设置对URL进行HTTP请求。
4. 文件消息构造器目前只能对群文件生效，私聊无效。



样例：

```java
EnhancedMessageChain chains = new FileMessageBuilder(eventPack)
	 .add("1234\n")
	 .add("1234\n", "5678\n")
     .add(messageChain)
	 .add(enhancedMessageChain)
	 .add(LocalFilePath)
	 .add(urlPath)
	 .add(file)
	 .send();（或.build();）
```



混合输入样例：

```java
EnhancedMessageChain chain = new FileMessageBuilder(eventPack)
    .add("1234")
    .add("1234","5678")
    .build();

EnhancedMessageChain chains = new VoiceMessageBuilder(eventPack, properties)//混合接龙
    .add(chain)//上面的消息链
    .add(VoiceLocalPath)
    .send();
```



#### HTTPProperties类型

该类型是为了对于一些链接质量不好的URL提供高级HTTP属性设置，例如HOST，Cookie等。如URL链接质量很好，可以不写。

该变量具有以下以下属性：

| 类型                | 属性              | 作用         | 默认值             |
| ------------------- | ----------------- | ------------ | ------------------ |
| int                 | Timeout           | 请求最大时间 | 3000               |
| String              | RequestMethod     | 请求方法     | GET                |
| Map<String, String> | RequestProperties | 属性设置     | 引擎伪装和保持连接 |

注：

1. 引擎伪装和保持连接指：

```java
"User-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.72 Safari/537.36"
"Connection", "keep-alive"
```

2. 以上默认值会在类型实例化时自动使用，如有需求，复写即可。

每种属性拥有以下操作方法：

| 返回类型            | 方法                       | 说明                           |
| ------------------- | -------------------------- | ------------------------------ |
| Map<String, String> | getRequestProperties()     | 获取HTTP属性Map                |
| void                | setRequestProperties()     | 添加HTTP属性，如果已存在则复写 |
| int                 | getRequestPropertiesSize() | 获取HTTP属性Map长度            |
| int                 | getTimeout()               | 获取最大超时时间设置           |
| void                | setTimeout()               | 设置最大超时时间               |
| String              | getRequestMethod()         | 获取请求方法                   |
| void                | setRequestMethod()         | 设置请求方法                   |



使用样例：

```java
HttpProperties properties = new HttpProperties();//实例化
properties.setTimeout(4000);//设置请求超时时间
properties.setRequestProperties("Connection", "keep-alive");//详细设置
...

//在Builder构造函数中使用
EnhancedMessageChain chains = new FileMessageBuilder(eventPack, properties)
	 .add(urlPath)
	 .send();
```



#### 消息模板库

消息构造器由于需要自定义消息格式，在某些简单消息构成时反复构造颇为麻烦。所以MiraiBoot也为大家提供了消息模板，用户只需要提供文字消息和素材来源即可，程序会自动构造并发送。

该模板库拥有以下模板方法：

| 方法                                    | 参数                                                         | 说明                                                         |
| --------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ImageMessageSender                      | MessageEventPack eventPack, <br />String ImagePath, <br />HttpProperties... properties | 单张图片消息模板                                             |
| ImageMessageSender                      | MessageEventPack eventPack, <br />MessageChain chain, <br />String ImagePath, <br />HttpProperties... properties | 单张图文消息连接器，需要自己先行构造MessageChain消息链。     |
| ImageMessageSender_asc                  | MessageEventPack eventPack, <br />String message, <br />String ImagePath, <br />HttpProperties... prpperties | 单张图文消息模板（正序）<br />例：<br /><br />文字<br />图片 |
| ImageMessageSender_desc                 | MessageEventPack eventPack, <br />String message, <br />String ImagePath, <br />HttpProperties... prpperties | 单张图文消息模板（倒序）<br />例：<br /><br />图片<br />文字 |
| ImageMessageSender_surround             | MessageEventPack eventPack, <br />String message_before, <br />String message_after, <br />String ImagePath, <br />HttpProperties... properties | 单张图文消息模板（文字环绕）<br />例：<br /><br />文字<br />图片<br />文字 |
| ImageMessageSender_multiImg             | MessageEventPack eventPack, <br />String messages, <br />String[] ImagePath, <br />HttpProperties... properties | 多张图片消息模板（正序）<br />例：<br /><br />文字<br />图片<br />图片 |
| ImageMessageSender_multiImg_msgDesc     | MMessageEventPack eventPack, <br />String messages, <br />String[] ImagePath, <br />HttpProperties... properties | 多张图文消息模板（倒序）<br />例：<br /><br />图片<br />图片<br />文字 |
| ImageMessageSender_multiImg_msgSurround | MessageEventPack eventPack, <br />String message_before, <br />String message_after, <br />String[] ImagePath, <br />HttpProperties... properties | 多张图文消息模板（文字环绕）<br />例：<br /><br />文字<br />图片<br />图片<br />文字 |
| ImageMessageSender_multiImgMsgList      | MessageEventPack eventPack, <br />String[] messages, <br />String[] ImgPath, <br />HttpProperties... properties | 多张图文消息模板（List样式）<br />例：<br /><br />图片<br />文字<br />图片<br />文字 |
| VoiceMsgSender                          | MessageEventPack eventPack, <br />String path, <br />HttpProperties... properties | 单条语音消息模板                                             |



#### MultipleParameterException异常

MultipleParameterException（多余的参数）是MiraiBoot新增的异常。可能会在使用图文消息模板库时抛出。

报文样例如下：

```
Caused by MultipleParameterException:
	Parameter "HTTPProperties" need 1 but found 2.
```

原因可能是因为目标方法的参数提供了多余的参数，如果无法避免，可以考虑使用[@ExceptionHandler](异常处理)注解对该异常进行监听和编写该异常处理方案。



## 上下文监听

上下文监听器的注册有两种方式，这里介绍其中种——在消息事件处理器中注册：

```java
@EventHandlerComponent
public class TestNext {

  @EventHandler(target = "搜图")
  @MessagePreProcessor(filterType = MessagePreProcessorMessageType.Image)
  public void testTimeOut(MessageEventPack eventPack, PreProcessorData data) {
    List<SingleMessage> filter = data.getClassified();
    if (filter.isEmpty()) {
      eventPack.reply("图呢");
      eventPack.onNextNow(new EventHandlerNext() {
        @Override
        @MessagePreProcessor(filterType = MessagePreProcessorMessageType.Image)
        public ListeningStatus onNext(MessageEventPack eventPack, PreProcessorData nextData) {
          if (nextData.getText().contains("没有")) {
            eventPack.reply("停止监听");
            return ListeningStatus.STOPPED;
          }
          List<SingleMessage> filter = nextData.getClassified();
          if (filter.isEmpty()) {
            eventPack.reply("等着呢 还是没有");
            return ListeningStatus.LISTENING;
          }
          SingleMessage image = filter.get(0);
          eventPack.reply(new PlainText("接收到图片\n"), image);
          return ListeningStatus.STOPPED;
        }

        @Override
        public void onTimeOut(MessageEventPack eventPack, PreProcessorData nextData) {
          eventPack.reply("已经超时 停止监听");
        }
      }, data, 1 * 60 * 1000L, -1);
      return;
    }
    SingleMessage image = filter.get(0);
    eventPack.reply(new PlainText("接收到图片\n"), image);
  }
}
```

以上是一个图片接收器的示例，机器人接受到指令搜图后触发监听器，`@MessagePreProcessor`过滤消息中的图片内容保存至`PreProcessorData`中，如果过滤后的消息List为空，表示发送消息搜图时并没有带上图片，于是**注册**了一个上下文监听器等待消息发送者发送图片。

在上下文监听器中，同样使用了`@MessagePreProcessor`过滤消息中的图片内容，同时，监听器还会判断接下来消息中是否包含文本内容"没有"，如果包含，那么返回`ListeningStatus.STOPPED`结束监听。

`1 * 60 * 1000L`表示监听1min，超时时调用`onTimeOut`发送消息停止监听。

更详细的内容可以看注释

## 异常处理

通过对类加上`@ExceptionHandlerComponent`的注解，并对其中的方法加上`@ExceptionHandler`的注解，可以将一个方法注册为异常处理器。例如：

```
@ExceptionHandlerComponent
public class TestException {
  @ExceptionHandler(targets = IllegalArgumentException.class)
  public void testException(Throwable e) {
    BotManager.getInstance().get(1231213L).getGroup(1002123124182L).sendMessage("error: " + e.getMessage() + " priority: 0");
  }
}
```

该异常处理器的功能是对`IllegalArgumentException`异常的处理，发生异常时向群1002123124182发送异常信息。

**注意**当异常没有对应的异常处理器时，不会被抛出，但是会打印stacktrce。

**注意**目前的异常处理器执行的是精确匹配，即全类名必须相等时异常处理器才会被触发。

以上的代码使用了两个注解，下面将详细介绍它们的参数和用法

#### @ExceptionHandlerComponent

将该类标记为异常处理类，启动时将会在该类下将标注了`@ExceptionHandler`注解的方法注册为异常处理器

| 类型 | 属性名 | 默认值 | 说明                                                   |
| ---- | ------ | ------ | ------------------------------------------------------ |
| int  | value  | 0      | 当@ExceptionHandler的priority未设置(为默认的0)时取该值 |

#### @ExceptionHandler

将受到注解的方法指定为异常处理器

| 类型                         | 属性名   | 默认值 | 说明             |
| ---------------------------- | -------- | ------ | ---------------- |
| Class<? extends Exception>[] | targets  | {}     | 要处理的异常列表 |
| int                          | priority | 0      | 触发优先级       |

##### `targerts`：

需要监听的异常类数组

##### `priority`：

处理器的优先级，当同一个异常有多个处理器时，将会按顺序从高优先级至低优先级依次执行，高优先级的处理器方法可以返回一个boolean值，主动阻止低优先级异常处理器的执行，但是**无法**阻止同优先级的处理器。

## 自动初始化

通过对类加上`@AutoInit`注解，并实现一个静态的`init`方法，可实现类的自动初始化功能。例如：

```
@AutoInit
public class TestInit {
  /**
   * @param config 配置文件
   */
  public static void init(ConfigFile config) {
    your code
  }
}
```

该方法将会在系统启动的最后，所有机器人被注册而未登录时开始执行(最后一个步骤是登录Bot)。

意味着此时已经可以访问miraiboot所管理的所有实例，包括事件处理器、异常处理器、全局配置和Bot实例。

#### `@AutoInit`：

| 类型 | 属性名 | 默认值 | 说明                                       |
| ---- | ------ | ------ | ------------------------------------------ |
| int  | value  | 0      | 初始化的优先级，数字**越大越先**进行初始化 |

## 内部类

### BotManager

用于对 Bot进行统一管理，只有一个全局实例

| 方法原型                    | 说明                                              |
| --------------------------- | ------------------------------------------------- |
| getInstance():BotManager    | 获取全局实例                                      |
| get(long):Bot               | 根据qq号获取Bot实例                               |
| getAllBot():Collection<Bot> | 获取所有Bot实例                                   |
| register(long, Bot):void    | 注册一个Bot实例                                   |
| remove(long):Bot            | 根据qq号移除一个Bot实例                           |
| logout(long):Bot            | 将qq号对应的Bot实例登出                           |
| logoutAll():void            | 登出所有Bot实例                                   |
| login(long):boolean         | 根据qq号登入一个Bot实例(Bot实例不存在时返回false) |
| loginAll():void             | 登入所有Bot实例                                   |

### EventHandlerManager

对事件处理器进行统一管理，只有一个全局实例。

具体方法请参考注释

### ExceptionHandlerManager

对异常处理器进行统一管理，只有一个全局实例。

具体方法请参考注释

### GlobalConfig

存储配置文件中miraiboot.config的配置内容。

具体方法请参考注释

### FileUtil

用于获取资源文件夹(data)下的内容

| 方法原型                                   | 说明                                            |
| ------------------------------------------ | ----------------------------------------------- |
| getResourceFile(String):File               | 根据文件名获取data文件夹下的文件                |
| getResourceFile(String, String):File       | 根据文件名获取data文件夹子文件夹下的文件        |
| getVoiceResourceFile(String):File          | 根据文件名获取data/voices文件夹下的文件         |
| getVoiceResourceFile(String， String):File | 根据文件名获取data/voices文件夹子文件夹下的文件 |
| getImageResourceFile(String):File          | 根据文件名获取data/images文件夹下的文件         |
| getImageResourceFile(String， String):File | 根据文件名获取data/images文件夹子文件夹下的文件 |
| getConfigFile():File                       | 获取配置文件                                    |

具体方法请参考注释
