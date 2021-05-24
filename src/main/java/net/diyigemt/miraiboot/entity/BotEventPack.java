package net.diyigemt.miraiboot.entity;

import net.mamoe.mirai.event.events.BotEvent;

/**
 * <h2>对Bot事件的简单封装</h2>
 * @author diyigemt
 * @since 1.0.0
 */
public class BotEventPack {

  private BotEvent event;

  public BotEventPack(BotEvent event) {
    this.event = event;
  }

  public BotEvent getEvent() {
    return event;
  }

  public void setEvent(BotEvent event) {
    this.event = event;
  }
}
