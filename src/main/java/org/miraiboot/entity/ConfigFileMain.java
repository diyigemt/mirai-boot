package org.miraiboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigFileMain {
  List<ConfigFileBot> bots;
  ConfigFileLogger logger;
}
