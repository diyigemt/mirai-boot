## 自动初始化

通过对类加上`@AutoInit`注解，并实现一个静态的`init`方法，可实现类的自动初始化功能。例如：

```java
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