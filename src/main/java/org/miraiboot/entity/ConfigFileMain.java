package org.miraiboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigFileMain {
  List<ConfigFileBot> bots;
  ConfigFileLogger logger;
  Map<String, Object> configs;
}
