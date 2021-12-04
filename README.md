## xm声明

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

## bug

项目包名与引入的依赖重名时会导致包扫描出现问题

当上下文监听器处理事件出现异常时，仍然会继续监听目标    需要更改为用户自定义

## 安装教程

在maven中添加依赖

```xml
<dependency>
        <groupId>net.diyigemt.miraiboot</groupId>
        <artifactId>mirai-boot</artifactId>
        <version>1.0.7</version>
</dependency>
```

你也可以去 [releases](https://github.com/diyigemt/mirai-boot/releases/)下载jar包并导入项目。
**注：第一次miraiboot需要引入mirai-login-solver-selenium这个依赖以完成新设备的滑动验证**

bot的设备信息文件 `*.json`将会存放在/config/qq号/*.json文件中，若是从其他框架迁移，可直接将json文件放在该文件夹下，并在配置文件中指定文件名即可，无需再次引入`mirai-login-solver-selenium`	

## 快速上手

创建一个主类

```java
import net.diyigemt.miraiboot.annotation.MiraiBootApplication;
import net.diyigemt.miraiboot.autoconfig.MiraiApplication;

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

**注意 第一次运行会在项目目录创建config cache和data三个文件夹**，并退出运行。需要在配置文件中指定机器人的qq号、密码和设备信息json文件名

config：保存miraiboot的配置文件和bot的device.json文件

cache：保存mirai-core的文件

data：保存miraiboot的资源文件 放在其中的文件可以很方便地通过工具类访问

data/plugin：保存插件

**关于bot设备信息**

bot的设备信息文件 `*.json`将会存放在/config/qq号/*.json文件中，若是从其他框架迁移，可直接将json文件放在该文件夹下，并在配置文件中指定文件名即可，无需再次引入`mirai-login-solver-selenium`	

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

本开发文档由Typora编写，建议使用Typora浏览已获得最佳体验

## 目录

[TOC]



## 系统启动

创建一个主类，并调用MiraiApplication的静态run方法。

```java
import net.diyigemt.miraiboot.annotation.MiraiBootApplication;
import net.diyigemt.miraiboot.autoconfig.MiraiApplication;

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

## 配置文件

系统的配置文件放置在`./config/application.yml`，其中包括的机器人的账号信息、一些设置的开关配置

具体配置项功能均有注释。

第一次运行时将会自动创建配置文件，并退出运行，需要手动在配置文件中指定机器人的账号信息。

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
public abstract class EventHandlerNext<T> {

  /**
   * <h2>上下文监听器方法</h2>
   * 该方法可以使用@MessageEventFilter注解来过滤信息
   * @param eventPack 事件本身
   * @param data 预处理器
   * @return 返回是否继续监听事件 ListeningStatus.LISTENING表示继续监听 STOPPED表示停止监听
   */
  public abstract ListeningStatus onNext(MessageEventPack eventPack, PreProcessorData<T> data);

  /**
   * <h2>监听器销毁时调用</h2>
   * @param eventPack 最后一次触发监听器的事件Event
   * @param data 最后一次触发监听器的PreProcessorData
   */
  public void onDestroy(MessageEventPack eventPack, PreProcessorData<T> data) { }

  /**
   * <h2>监听器超时时调用</h2>
   * @param eventPack 最后一次触发监听器的事件Event
   * @param data 最后一次触发监听器的PreProcessorData
   */
  public void onTimeOut(MessageEventPack eventPack, PreProcessorData<T> data) { }

  /**
   * <h2>监听器触发次数耗尽时调用</h2>
   * @param eventPack 最后一次触发监听器的事件Event
   * @param data 最后一次触发监听器的PreProcessorData
   */
  public void onTriggerOut(MessageEventPack eventPack, PreProcessorData<T> data) { }

  /**
   * <h2>在监听器处理过程中如果抛出异常由此方法处理</h2>
   * 默认直接交给全局异常处理器处理
   * @param e 异常
   * @param eventPack 触发的事件
   * @param data 触发事件的数据
   * @return 是否继续监听
   */
  public ListeningStatus onException(Throwable e, MessageEventPack eventPack, PreProcessorData<T> data) {
    ExceptionHandlerManager.getInstance().emit(e, eventPack, data);
    return ListeningStatus.STOPPED;
  }
}
```

| 方法原型                                                     | 说明                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| <T extends EventHandlerNext> onNext(T, PreProcessorData):void | 注册一个上下文监听器，使用全局配置的超时时间，监听本事件的发送者 |
| <T extends EventHandlerNext> onNext(T, long, PreProcessorData):void | 注册一个上下文监听器，设置超时时间为long，监听本事件的发送者 |
| <T extends EventHandlerNext> onNext(T, int, PreProcessorData):void | 注册一个上下文监听器，使用全局配置的超时时间，监听本事件的发送者，并至多触发int次 |
| <T extends EventHandlerNext> onNext(T, long, int, PreProcessorData):void | 注册一个上下文监听器，设置超时时间为long，监听本事件的发送者，并至多触发int次 |
| <T extends EventHandlerNext> onNext(long, T, long, int, PreProcessorData):void | 注册一个上下文监听器，设置超时时间为long，监听第一个long对应的群友，并至多触发int次 |

#### PreProcessorData<T>

`PreProcessorData`是对消息事件的处理结果，包含了解析出的指令、参数、纯文本和@MessagePreProcessor的预处理结果

| 属性         | 类型                | 说明                                         |
| ------------ | ------------------- | -------------------------------------------- |
| commandText  | String              | 匹配到的包含指令开头所有内容                 |
| command      | String              | 匹配到的指令                                 |
| args         | List<String>        | 匹配到的参数                                 |
| text         | String              | 消息纯文本                                   |
| classified   | List<SingleMessage> | 过滤出的消息内容                             |
| triggerCount | int                 | 上下文触发剩余次数(仅在上下文监听事件中有用) |
| data         | T                   | 使用自定义消息预处理器时的处理结果           |

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

| 类型                            | 属性名    | 默认值                      | 说明                                              |
| ------------------------------- | --------- | --------------------------- | ------------------------------------------------- |
| String                          | value     | ""                          | 匹配的内容，为空表示忽略                          |
| MessageFilterMatchType          | matchType | MessageFilterMatchType.NULL | 关键词的匹配类型，为NULL表示忽略                  |
| String[]                        | accounts  | {}                          | 若消息发送着不在列表内则不做响应，为空表示忽略    |
| String[]                        | groups    | {}                          | 若消息发送着不在列表内则不做响应，为空表示忽略    |
| String[]                        | bots      | {}                          | 若消息接受的bot不在列表内则不做响应，为空表示忽略 |
| boolean                         | isAt      | false                       | 是否当bot被at时才触发                             |
| boolean                         | isAtAll   | false                       | 是否at全体时才触发                                |
| boolean                         | isAtAny   | false                       | 是否有人被at时才触发，不一定是bot被at             |
| Class<? extends IMessageFilter> | filter    | MessageFilterImp.class      | 用户自定义的消息过滤器                            |

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

##### `filter`:

用户自定义的消息过滤器，可以与原属性并行生效

所有自定义的消息过滤器必须实现`IMessageFilter`接口，其中的`check`方法返回值标识过滤器是否通过

具体可查看`IMessageFilter`和`MessageFilterImp`的注释

#### @MessagePreProcessor

消息事件预处理器，对收到的消息事件进行预处理

| 类型                                     | 属性名        | 默认值                    | 说明                                                        |
| ---------------------------------------- | ------------- | ------------------------- | ----------------------------------------------------------- |
| boolean                                  | textProcessor | false                     | 将所有纯文本消息提取出来保存在PreProcessorData.text         |
| MessagePreProcessorMessageType[]         | filterType    | {}                        | 将对应类型的消息提取出来保存在PreProcessorData.classified中 |
| Class<? extends IMessagePreProcessor<?>> | filter        | MessageProcessorImp.class | 用户自定义的消息预处理器                                    |

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

##### `filter`:

用户自定义的消息预处理器，可以与原属性并行生效

所有自定义的消息预处理器必须实现`IMessagePreProcessor`接口，其中的parseMessage方法对消息进行预处理

具体可查看`IMessagePreProcessor`和`MessageProcessorImp`的注释

用户可以将处理结果放在

```java
PreProcessorData<T>.setData(T)
```

中，并在事件处理器中通过`T PreProcessorData<T>.getData()`获取自己的处理结果



## 消息的构造和发送

#### 消息链概念介绍

消息链（MessageChain）是指一种由多种类型元素组成的链表结构。任意一种继承Message类的元素均可成为链表中的一个节点，例如文字PlainText、语音Voice、图片Image、文件FileMessage等等，统称为SingleMessage。多个SingleMessage连接到一起，即为消息链。下图即是一条图文消息的消息链构成。

![](https://lychee.diyigemt.net/uploads/big/312aab6dde35cabb196d83317f74691c.jpg)

值得注意的是，SingleMessage并不一定是实际显示中的一条消息，它指的是一条消息中的一个元素。如果该消息由多个元素构成（例如图文消息，拥有图片和文字两种元素），则实际显示中一条消息是MessageChain。而且，对于语音元素和文件元素，本文将这两种元素成为特殊元素。由于QQ自身对特殊元素显示规则等原因，每条消息链只能接受一个特殊元素。一旦插入特殊元素，之前的图文元素会被忽略，如果插入多个特殊元素，实际显示时只会显示消息链中最后一个特殊元素。



#### MessageChain类型

MessageChain类型是Mirai-core中自带的默认消息链类型，用于存储各种元素的构成。详情请看[Mirai文档](https://github.com/mamoe/mirai/blob/dev/docs/Messages.md#%E6%B6%88%E6%81%AF%E7%B3%BB%E7%BB%9F)。下面列举一些常用的方法：

| 返回类型     | 方法                    | 参数                                | 说明                   |
| ------------ | ----------------------- | ----------------------------------- | ---------------------- |
| MessageChain | .plus()                 | Char, SingleMessage, MessageChain等 | 向消息链中添加消息元素 |
| String       | .serializeToMiraiCode() | 无                                  | 将消息转换为Mirai格式  |



#### EnhancedMessageChain类型

在[消息链概念介绍](消息链概念介绍)中我们指出了MessageChain关于特殊元素支持的不足之处，而在MiraiBoot中，我们对此进行了一些改进和封装，新增了一个类型：EnhancedMessageChain（加强消息链）。其本质为List<MessageChain>，用于支持特殊消息的混合插入。同时加入URL和本地路径识别，可以自动联网获取特殊元素素材进行发送，无需自行下载。



相比MessageChain，EnhancedMessageChain可以将图文信息和群文件以及语音元素混合插入，打破了MessageChain对于特殊元素不能添加多个的限制。同时EnhancedMessageChain也支持连接使用MessageChainBuilder构造完成的结果。但需要注意的是，由于特殊元素的素材体积参差不齐，个别差距悬殊、程序发送信息速度超过手动操作、接收方网络带宽限制、QQ显示消息采取先到先显示的规则等缘故，EnhancedMessageChain并不能保证在任何情况下接收方消息顺序和您安排的顺序保持一致。如果一定要求顺序，建议您使用MessageChain类型存储，当做一整条消息发送出去，但这仅仅支持图文。

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



在MiraiBoot中，新增了三种消息构造器为EnhancedMessageChain服务。其使用方法和原版MessageChainBuilder基本保持一致，同时加入了构造并发送的send（）方法。但MessageChainBuilder并不能为EnhancedMessageChain构造消息。介绍如下。

| 返回类型             | 构造器              | 参数                                        | 说明           |
| -------------------- | ------------------- | ------------------------------------------- | -------------- |
| EnhancedMessageChain | ImageMessageBuilder | EventPack（必填）, HttpProperties（可不填） | 图片消息构造器 |
| EnhancedMessageChain | VoiceMessageBuilder | EventPack（必填）, HttpProperties（可不填） | 语音消息构造器 |
| EnhancedMessageChain | FileMessageBuilder  | EventPack（必填）, HttpProperties（可不填） | 文件消息构造器 |

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
2. 请不要在一条加强消息链上添加过多图片，语音、群文件等元素，可能会因为消息接收方带宽限制和QQ消息同步规则使得接收方消息乱序，得不偿失（如果必要，可以考虑在两条特殊消息链组成的消息之间写一个延时降低机器人发消息的速度）。
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
Caused by org.miraiboot.exception.MultipleParameterException:
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
      }, data, 5* 1000L, -1);
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

更详细的内容可以看`MessageEventPack#onNext`、`EventHandlerNext`

## 定时任务

//TODO

## 异常处理

`miraiboot`的异常处理分为局部异常处理和全局异常处理

1. 局部异常处理指在`@EventHandlerComponet`注解的类中注册的`@ExceptionHandler`

   被注册的处理方法将会处理在该类中注册的事件处理器中抛出的异常，当没有对应的异常处理器是才转交由全局异常处理；

   对于上下文监听器，可以通过重载`EventHandlerNext`中的`onException`方法对监听处理中发生的异常进行处理，当然，如果你希望某些异常交由全局异常处理，可以调用父类的`super`方法并将异常传入

2. 全局异常处理指在`@ExceptionHandlerComponent`注解的类中注册的`@ExceptionHandler`

   被注册的处理器将会处理事件监听器中的所有异常，当异常没有对应的处理器时，将会简单的进行`printStackTrace`

通过对类加上`@ExceptionHandlerComponent`的注解，并对其中的方法加上`@ExceptionHandler`的注解，可以将一个方法注册为全局异常处理器。例如：

```java
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

**注意**异常处理器执行的匹配为`Cast`匹配，即当异常可以被`Cast`·为异常处理器处理的异常时将会被异常处理器捕获。

通过对被`@EventHandlerComponent`注解的类中的方法加上`@ExceptionHandler`注解，可以将该方法注册为局部异常处理器。例如:

```java
@EventHandlerComponent
public class TestException {
  @EventHandler(target = "error1")
  public void testSendError1(MessageEventPack eventPack) {
    throw new RuntimeException("测试");
  }

	@ExceptionHandler(RuntimeException.class)
  public void handlerError(RuntimeException e, MessageEventPack eventPack) {
    eventPack.reply("收到error: " + e.getMessage());
  }
}
```

以上的代码使用了两个注解，下面将详细介绍它们的参数和用法

#### @ExceptionHandlerComponent

将该类标记为异常处理类，启动时将会在该类下将标注了`@ExceptionHandler`注解的方法注册为异常处理器

| 类型 | 属性名 | 默认值 | 说明                                                   |
| ---- | ------ | ------ | ------------------------------------------------------ |
| int  | value  | 0      | 当@ExceptionHandler的priority未设置(为默认的0)时取该值 |

#### @ExceptionHandler

将受到注解的方法指定为异常处理器

| 类型                       | 属性名   | 默认值 | 说明                                         |
| -------------------------- | -------- | ------ | -------------------------------------------- |
| Class<? extends Exception> | vlaue    | 无     | 要处理的异常类                               |
| int                        | priority | 0      | 触发优先级                                   |
| String                     | name     | ""     | 可以根据name主动删除一个异常处理器(现在没用) |

##### `value`：

需要监听的异常类，所有可以Cast到该类的异常均会被捕获

##### `priority`：

处理器的优先级，当同一个异常有多个处理器时，将会按顺序从高优先级至低优先级依次执行，高优先级的处理器方法可以返回一个boolean值，主动阻止低优先级异常处理器的执行，但是**无法**阻止同优先级的处理器。

##### `name`:

一个异常处理器的唯一标识，可以通过该标识删除异常处理器

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



## 权限管理

MiraiBoot拥有一个简单的权限管理系统。满足日常机器人对QQ群的管理。



### 管理层权限

管理层权限是管理员和群主在权限控制台拥有的权限级别。

### 临时权限

临时权限是管理层对目标用户授权的主要方式，权限级别低于管理层权限，管理员和群主可以同时拥有管理层权限和临时权限，此时生效的是管理层权限。



### @CheckPermission注解

该注解是用于权限管理的注解，和@EventHandler注解一同使用，其被注解的方法会在执行前进行使用者的权限检查。如果权限不足，则本次命令会被拦截，不会执行。



@CheckPermission注解拥有以下几个变量：

| 类型     | 变量               | 默认值        | 说明                                                   |
| -------- | ------------------ | ------------- | ------------------------------------------------------ |
| String[] | allows             | null          | 允许列表，列表外的用户均无法使用【1】                  |
| String[] | block              | null          | 禁止列表，列表外的用户均可以使用【1】                  |
| boolean  | isStrictRestricted | false         | 禁止越级操作（包括同级别和自己）【2】                  |
| boolean  | isGroupOwnerOnly   | false         | 群主独占功能                                           |
| boolean  | isAdminOnly        | false         | 管理层独占功能【3】                                    |
| int      | FunctionID         | DEFAULT_INDEX | 方法ID，普通功能为正数，管理层功能为负数，±1已被占用。 |

各变量之间的优先级顺序（||之间的变量为MiraiBoot框架中规定的权限【4】）：

​	| at-> permission table -> | blocks -> allows -> isStrictRestricted -> isGroupOwnerOnly -> isAdminOnly

使用时只需在对应方法上加入该注解并且设定部分变量来控制即可。【5】



注：

1. 对于允许和禁止列表提供的应该是目标QQ号，不识别昵称等其它输入

2. 越级操作：指低权限成员对比自身权限高的目标成员操作。

   ​	成员权限分布和QQ群规则保持一致，即：群主 -> 管理员 -> 普通群员。

   当禁止越级操作关闭时，权限管理系统将允许下克上，例如：管理员可以对群主操作，群员对管理员操作等。

3. 管理层：指群主和管理员

4. 规定的权限：指独立存在的优先级，高于任何其它变量，只有程序编写者拥有该权限的控制权。

5. 被@CheckPermission注解且开启禁止越级操作时，命令的方法必须拥有一个@目标成员作为参数传入比较，否则会因没有目标对象进行对比而报错，且该@目标成员参数应在命令语句中所有@操作的末端。

6. 权限控制台无法对机器人执行禁止使用操作。



使用样例：

```java
@EventHandler(target = "Hello")//必须和EventHandler搭配使用
@CheckPermission(isAdminOnly = true, isStrictRestricted = true, FunctionID = 2)
public void Hello(MessageEventPack eventPack, PreProcessorData data){
    //do something...
}
```

以上样例表明了：

权限管理对命令Hello生效，只有管理层拥有该命令的使用权，禁止越级操作（如管理员对群主操作），该命令对应的响应方法ID注册为2。



### 权限控制台

有权限检查自然就有权限控制台，以下是MiraiBoot中权限控制台的使用规则：

1. 权限控制台只对群主和管理员开放，普通成员无法使用。
2. 当群成员被授予管理员时，权限控制台会自动对目标成员开放。
3. 权限控制台强制开启禁止越级操作，无法修改。
4. 权限控制台无法通过临时权限的方式对普通群员授予使用权。



以下是权限控制台的使用方法：

| 命令                                       | 说明                                        |
| ------------------------------------------ | ------------------------------------------- |
| permit 命令名称/别名 on/off @目标成员      | 解禁/禁止目标成员的xxx命令使用              |
| permit assign 命令名称/别名 @目标成员      | 对目标成员授予无限制次数的xxx命令临时使用权 |
| permit assign 次数 命令名称/别名 @目标成员 | 对目标成员授予有次数限制的xxx命令临时使用权 |
| permit cancel 命令名称/别名 @目标成员      | 对目标成员解除xxx命令的临时使用权           |



注：

1. 权限优先级：

   permit on/off（管理层权限） -> 临时权限

   当管理层权限和临时权限同时存在时，会执行管理层权限的限制。

2. 当目标被管理层禁止使用xxx命令后又被赋予该命令的临时使用权时，禁止使用的限制会自动解除。

3. 临时使用权支持升降级别，从无限制次数的临时权限降级到有次数限制的临时权限，反之则是提权。但提权后仍属于临时权限范畴。



使用样例：

```java
permit reply off @1235472346
permit assign reply @1235472346//注意：此时管理层禁止使用的限制会被解除
permit assign reply 3 @1235472346//降级为带次数限制的临时权限
permit cancel reply @1235472346
```



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



## 插件式开发

MiraiBoot支持插件式开发模式

若采用插件式开发，miraiboot的依赖版本需要为1.0.5+，需要去[Release](https://github.com/diyigemt/mirai-boot/releases/)中下载miraiboot的核心包mirai-boot-x.x.x-core.jar单独运行，并将打包后的插件放置在./data/plugin中

### MiraiBootPlugin插件类

​	MiraiBootPlugin插件类是在主类上继承使用的类，继承MiraiBootPlugin的主类打包后将会被MiraiBoot识别为MiraiBoot插件。

该类拥有以下变量：

| 类型          | 名称               | 说明                                      |
| ------------- | ------------------ | ----------------------------------------- |
| List<JarFile> | PluginDependencies | 该变量为存储用户自定义加载项的JarFile对象 |
| Boolean       | UEFIMode           | UEFI模式开关，默认开启                    |

注：[UEFI模式](UEFI和Legacy两种加载模式)

该类拥有以下方法：

| 返回类型 | 方法名              | 参数          | 说明                                                        |
| -------- | ------------------- | ------------- | ----------------------------------------------------------- |
| void     | onLoad（）          | null          | 插件加载前执行的方法，默认为空                              |
| void     | addDependencies（） | List<JarFile> | 在onLoad中使用该方法，MiraiBoot会按照List顺序加载jar包【1】 |

​	注：

 	1. addDependencies方法使用的加载方式为Legacy（全包扫描），无法修改。



使用样例：

```java
@MiraiBootApplication(description = "测试项目")
public class Main extends MiraiBootPlugin{//在主类上继承插件类
    
    @Override
    public void onLoad(){//重写onLoad方法，MiraiBoot将会在加载该插件被加载前执行该方法代码块中的内容
        UEFIMode = false;//该插件将使用全包扫描的方式加载
        
        List<JarFile> files = new ArrayList<>;
        files.add(new JarFile("Put your path here."));//定义List并添加
        addPluginDependencies(file);//在插件加载前，MiraiBoot会优先加载List中的内容。
    }
  public static void main(String[] args){
    MiraiApplication.run(Main.class, args);
  }
}
```

当然，以上的`@MiraiBootApplication`和`static main`方法不是必须的，这里只是为了方便将插件和miraiboot核心写到一起了

使用结果：

```java
2021-06-03 17:42:04 I/mirai-boot-status: 测试项目
2021-06-03 17:42:04 I/mirai-boot-status: 开始读取配置文件
2021-06-03 17:42:04 I/mirai-boot-status: 配置文件读取成功
2021-06-03 17:42:04 I/mirai-boot-status: 开始扫描插件
2021-06-03 17:42:04 I/mirai-boot-status: 发现插件：qqbot-re-1.0.2-jar-with-dependencies.jar
2021-06-03 17:42:04 I/mirai-boot-status: 解析到自定义加载项，正在加载 //加载List内容的提示
2021-06-03 17:42:04 I/mirai-boot-status: 正在加载： qqbot-re-1.0.2-jar-with-dependencies.jar//开始加载插件本身
2021-06-03 17:42:04 I/mirai-boot-status: 插件: qqbot-re-1.0.2-jar-with-dependencies.jar，加载成功//加载成功
2021-06-03 17:42:04 I/Mirai: Mirai 正在使用桌面环境. 如遇到验证码将会弹出对话框. 可添加 JVM 属性 `mirai.no-desktop` 以关闭.
...
```

### UEFI和Legacy两种加载模式

​	这两个词想必各位电脑爱好者都有所耳闻，这两个词指个人计算机启动模式，常见于主板的BIOS中。

​	UEFI（统一可扩展固件接口）：当然严格说应该叫UEPI，因为插件和固件本质上有很大的区别，叫UEFI只是便于认识。该模式下，MiraiBoot只会加载和扫描主类所在的包和该包下的子包，并将所有扫描到的class文件加载进JVM中【1】。在主类包之外的Class文件将不会扫描和加载【2】。推荐使用此模式（该模式为默认加载方式）加载插件。此模式优点在于，跳过了所有依赖包，扫描速度极快，而且可以甩掉全包加载有时需要添加间接依赖的问题。

​	Legacy（全包扫描）：该模式下，MiraiBoot将使用传统的加载方式——即遍历jar包中的所有Class文件并加载和扫描【3】，速度很慢（具体取决于jar中文件目录复杂程度），该模式只适合用来加载某些依赖，并不适合用来加载插件。

​	

​	使用方法：在MiraiBoot框架根目录创建data文件夹，在data文件夹中创建plugin子文件夹，将MiraiBoot插件放进plugin子文件夹（您也可以直接运行MiraiBoot等待登录完成后关闭，此时MiraiBoot会帮你新建文件系统结构）。下次程序启动时会自动检索该目录的jar文件并解析onLoad方法以应用设置。



​	注：

1. class文件标准:不是所有带class后缀的文件都是被JVM承认的，JVM会通过检查16进制元数据下头四位数据来识别是否可以作为运行时依赖被JVM加载，您需要保证头四位元数据为：

```
CAFEBABE
```

​	使用十六进制打开样例：

```assembly
00000000: cafe babe 0000 0034 001f 0a00 0500 1909  .......4........
00000010: 0004 001a 0800 1b07 001c 0700 1d07 001e  ................
```

​		

​	可以看见头八位为CAFEBABE，是可以被JVM加载的class文件。您可以通过此方式来判断对应的class类可否被JVM加载，类所属的依赖是否是运行时使用。

2. 您可能会担心只加载主类包会不会导致依赖没有被加载？其实并不会，加载class时存在迭代的，如果A类Import了B类，将自动加载前提B类，以此类推，直到前提全部加载完成后才会加载A类。
3. 全包扫描模式有个比较恼火的问题，无法排除间接依赖（指您依赖的包自身依赖了其它的包），由于是整个jar包遍历加载，即使是依赖中您从来没有引用的class也会被加载，这些无用的class加载时还可能会弹出大量的缺少间接依赖提示，给插件带来不必要的臃肿。所以再次提醒您，如无特殊需求，不要使用该模式加载插件。



#### 两种模式查找的对象

无论是上面哪种模式，MiraiBoot都会在加载Class的同时寻找该class中是否含有以下注解：

@EventHandlerComponent

@ExceptionHandlerComponent

@AutoInit

MiraiBoot将会对扫描到以上注解的class进行相应的注册和初始化等操作。



#### 加载规则

1. MiraiBoot在加载插件时不允许出现任何有关缺少依赖的异常抛出，只要抛出哪怕只有一条，MiraiBoot便会直接放弃该插件的所有加载并屏蔽该插件所有功能。

2. 自定义加载项只能使用Legacy模式加载。
3. 加载时，JVM会先检查该类是否已被加载过，如果已被加载，即使后者是新版本，它也不会加载后者。



​	样例：

​	假设您插件工程有以下结构:

```java
	src
		java
			com.example.mybot
        		TextFunction.java
				core
					Class1.java
					Class2.java
					Class3.java
				Main.java
    		com.example.lib
        			Class4.java
        
    External libraries
    	Maven: xxx.jar
```

​	

此时您对插件打包后使用默认的UEFI启动方式时：

​	MiraiBoot会加载com.example.mybot包下所有的class，不会加载外部依赖xxx.jar和com.example.lib包。

而加载时也会给你一个UEFI标识

```java
2021-06-03 19:09:38 I/mirai-boot-status: 正在加载： qqbot-re-1.0.2-jar-with-dependencies.jar(UEFI)//UEFI标识
```



如果您关闭了UEFI使用Legacy模式加载时：

​	以上工程中的所有文件都会被加载。

加载时的提示也是普通的

```
2021-06-03 19:09:38 I/mirai-boot-status: 正在加载： qqbot-re-1.0.2-jar-with-dependencies.jar//没有标识
```



### MiraiBoot插件打包规范

#### 打包方法

​	虽说这个大家都会，但为了照顾新人，这里还是介绍一种测试过程中使用的打包插件：maven-assembly-plugin，它可以将您构造的包结构和依赖包额结构合并在一起。

```xml
<Project>
    <dependencies>
        ...//您加入的依赖
    </dependencies>
	<build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>3.3.0</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>com.example.mybot.Main</mainClass>//您的主类
                            <addClasspath>true</addClasspath>
                        </manifest>
                    </archive>
                    <appendAssemblyId>true</appendAssemblyId>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                </configuration>
                <executions>
                    <execution>
                        <id>make-assembly</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</Project>
```



#### 打包时需要排除的依赖

​	MiraiBoot本身已经使用了一些依赖如下，无需重复添加依赖，否则可能会出现一些多重加载的异常：

```xml
<dependencies>
        <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-simple -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>1.7.30</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->

        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-stdlib</artifactId>
            <version>1.4.32</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/com.github.pagehelper/pagehelper -->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
            <version>5.2.0</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.12.3</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
            <version>2.12.3</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-annotations</artifactId>
            <version>2.12.3</version>
        </dependency>


        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.5.0</version>
            <scope>test</scope>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.16</version>
            <scope>compile</scope>
        </dependency>

        <dependency>
            <groupId>net.mamoe</groupId>
            <artifactId>mirai-core-jvm</artifactId>
            <version>2.6.4</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.yaml/snakeyaml -->
        <dependency>
            <groupId>org.yaml</groupId>
            <artifactId>snakeyaml</artifactId>
            <version>1.28</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc -->
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.34.0</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.j256.ormlite/ormlite-core -->
        <dependency>
            <groupId>com.j256.ormlite</groupId>
            <artifactId>ormlite-core</artifactId>
            <version>5.3</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.j256.ormlite/ormlite-jdbc -->
        <dependency>
            <groupId>com.j256.ormlite</groupId>
            <artifactId>ormlite-jdbc</artifactId>
            <version>5.3</version>
        </dependency>

    </dependencies>
```



以及在插件工程中的MiraiBoot依赖本身

```xml
        <dependency>
            <groupId>net.diyigemt.miraiboot</groupId>
            <artifactId>mirai-boot</artifactId>
            <version>1.0.5</version>
            <scope>provided</scope>
        </dependency>
```



在需要排除的依赖下加入<scrope>标签，打包时即可排除。样例如上。

然后在Maven Lifecycle中执行Package即可。

### 开发时常见的问题

#### ClassNotFoundException和ClassDefNotFoundErr

下面是一个错误例子：

```
2021-06-03 17:37:51 E/Mirai-boot-status: org.apache.ibatis.executor.loader.cglib.CglibProxyFactory$EnhancedDeserializationProxyImpl：加载失败
缺少依赖：net.sf.cglib/proxy.MethodInterceptor
2021-06-03 17:37:51 E/mirai-boot-status: 插件：qqbot-re-1.0.2-jar-with-dependencies.jar 加载失败，缺少部分依赖
```

可以导致该异常的原因有很多，常见的原因如下：

1. 您的插件包里确是缺少依赖或路径有误，可以使用压缩软件按包路径依次打开看看有没有该依赖，没有的话请在pom.xml中加入缺少的依赖
2. 您可能把一些不在运行时工作的依赖加入了jar包，例如lambook
3. 您的依赖可能存在版本兼容性或依赖冲突的问题



以上仅仅是提示，实际情况还请您多发散思维，多考虑一些情况。



#### 加载时错误提示解释

| 错误提示样例                                                 | 解释                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| myBotPlugin.jar: 未知的MiraiBoot插件格式                     | 该错误是因为MANIFEST.MF中Main-Class属性记载的主类没有继承MiraiBootPlugin插件类。 |
| 自定义加载项: myBotPlugin.jar: 加载失败，缺少部分依赖，已放弃该插件所有加载 | 因为您在onLoad中调用addDependencies方法加载的jar中出现了缺少依赖异常，导致MiraiBoot停止了该插件和自定义加载项的工作。 |
| 插件: myBotPlugin.jar: 启动失败，缺少部分依赖                | 因为插件本体加载时出现了缺少依赖异常，导致MiraiBoot放弃该插件的加载。 |
| org.apache.ibatis.executor.loader.cglib.CglibProxyFactory$EnhancedDeserializationProxyImpl：加载失败<br/>缺少依赖：net.sf.cglib/proxy.MethodInterceptor | 目标Class在加载Import的间接依赖时失败                        |



#### 

## MiraiBoot控制台

#### TODO：@ConsoleCommand注解



#### 默认命令

当MiraiBoot登录QQ完成后，控制台即可使用，控制台本身拥有以下命令：

| 指令   | 参数                 | 说明                      |
| ------ | -------------------- | ------------------------- |
| exit   | null                 | 停止所有BOT活动并关闭程序 |
| plugin | [list\|load\|unload] | 插件管理命令              |

##### plugin命令

​	MiraiBoot中拥有一个插件管理器，用于加载和管理所有MiraiBoot插件。该管理器由plugin命令进行操作。

​	以下是plugin命令的格式：

| 指令                            | 说明                                   |
| ------------------------------- | -------------------------------------- |
| plugin list                     | 查看所有已加载的插件                   |
| plugin load [path]              | 加载[path]指定的插件（相对或绝对路径） |
| plugin unload [packageName.jar] | 卸载名为[packageName.jar]的插件        |
