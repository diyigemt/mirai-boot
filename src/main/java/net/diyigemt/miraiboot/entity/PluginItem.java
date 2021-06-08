package net.diyigemt.miraiboot.entity;

import lombok.Data;

import java.lang.annotation.Annotation;

/**
 * <h2>插件信息统计类</h2>
 * @author Haythem
 */
@Data
public class PluginItem {

    private String PackageName = null;

    private String ClassName = null;

    private Annotation AnnotationData = null;
}
