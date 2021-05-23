package org.miraiboot.config;

import org.junit.jupiter.api.Test;
import org.miraiboot.Main;
import org.miraiboot.entity.ConfigFile;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class TestReadYaml {
  @Test
  public void testReadConfig() {
    InputStream stream = Main.class.getResourceAsStream("/application-example.yml");
//    ConfigFile configFile = new Yaml().loadAs(stream, ConfigFile.class);
//    System.out.println(configFile.toString());
  }
}
