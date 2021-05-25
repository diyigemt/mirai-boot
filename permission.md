## 权限管理

通过对消息事件处理器添加`@CheckPermission`注解，可对该指令启用动态权限管理功能。

```java
@EventHandlerComponent
public class Test {
	@EventHandler(target = "复读")
	@CheckPermission(blocks = "123123123", isAdminOnly = true, functionId = 2)
	public void test(MessageEventPack eventPack, PreProcessorData data) {
		eventPack.reply(eventPack.getMessage());
	}
}
```

上述指令将忽略对qq号为123123123的响应，同时只允许管理员操作，并指定指令对应的权限id为2。

该注解的所有参数如下：

#### `@CheckPermission`：

| 类型     | 属性名             | 默认值 | 说明                                             |
| -------- | ------------------ | ------ | ------------------------------------------------ |
| String[] | allows             | {}     | 允许操作的qq号列表，非空将会只允许列表中的人操作 |
| String[] | blocks             | {}     | 禁止操作的qq号列表，为空表示不检查               |
| boolean  | isStrictRestricted | false  |                                                  |
| boolean  | isGroupOwnerOnly   | false  | 是否只允许群主操作                               |
| boolean  | isAdminOnly        | false  | 是否只允许管理员操作(包括群主)                   |
| int      | functionId         |        |                                                  |

以上参数的优先级为：

permission table -> blocks -> allows -> isStrictRestricted -> isGroupOwnerOnly -> isAdminOnly