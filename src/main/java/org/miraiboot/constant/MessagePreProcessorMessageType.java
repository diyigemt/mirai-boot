package org.miraiboot.constant;

/**
 * <h2>消息预处理器过滤类型</h2>
 * 与mirai核心的消息类型完全对应
 * @author diyigemt
 * @since 1.0.0
 * @see org.miraiboot.annotation.MessageFilter
 */
public enum MessagePreProcessorMessageType {
  /**
   * 纯文本<br/>
   * https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/PlainText.kt
   * @see net.mamoe.mirai.message.data.PlainText
   */
  PlainText,
  /**
   * 图片
   * 包括群图片 好友图片<br/>
   * https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/Image.kt
   * @see net.mamoe.mirai.message.data.GroupImage
   * @see net.mamoe.mirai.message.data.FriendImage
   */
  Image,
  /**
   * 艾特(@)人的消息<br/>
   * https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/At.kt
   * @see net.mamoe.mirai.message.data.At
   */
  At,
  /**
   * 艾特(@)全体的消息<br/>
   * https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/AtAll.kt
   * @see net.mamoe.mirai.message.data.AtAll
   */
  AtAll,
  /**
   * 自带的表情栏的表情<br/>
   * https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/Face.kt
   * @see net.mamoe.mirai.message.data.Face
   */
  Face,
  /**
   * 闪照<br/>
   * https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/FlashImage.kt
   * @see net.mamoe.mirai.message.data.FlashImage
   */
  FlashImage,
  /**
   * 戳一戳<br/>
   * https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/PokeMessage.kt
   * @see net.mamoe.mirai.message.data.PokeMessage
   */
  PokeMessage,
  /**
   * vip才能用的表情<br/>
   * https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/VipFace.kt
   * @see net.mamoe.mirai.message.data.VipFace
   */
  VipFace,
  /**
   * 快应用<br/>
   * https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/RichMessage.kt
   * @see net.mamoe.mirai.message.data.LightApp
   */
  LightApp,
  /**
   * 语音<br/>
   * https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/Voice.kt
   * @see net.mamoe.mirai.message.data.Voice
   */
  Voice,
  /**
   * 表情市场中的表情<br/>
   * https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/MarketFace.kt
   * @see net.mamoe.mirai.message.data.MarketFace
   */
  MarketFace,
  /**
   * 合并转发的消息<br/>
   * https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/ForwardMessage.kt
   * @see net.mamoe.mirai.message.data.ForwardMessage
   */
  ForwardMessage,
  /**
   * 另一种消息类型 不常用<br/>
   * https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/RichMessage.kt
   * @see net.mamoe.mirai.message.data.SimpleServiceMessage
   */
  SimpleServiceMessage,
  /**
   * 音乐分享<br/>
   * https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/MusicShare.kt
   * @see net.mamoe.mirai.message.data.MusicShare
   */
  MusicShare,
  /**
   * 骰子消息<br/>
   * https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/Dice.kt
   * @see net.mamoe.mirai.message.data.Dice
   */
  Dice,
  /**
   * 文件消息<br/>
   * https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/FileMessage.kt
   * @see net.mamoe.mirai.message.data.FileMessage
   */
  FileMessage
}
