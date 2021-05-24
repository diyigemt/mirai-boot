package net.diyigemt.miraiboot.utils.builder;

import net.diyigemt.miraiboot.entity.HttpProperties;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.utils.ExternalResource;
import net.diyigemt.miraiboot.utils.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * <h2>外部资源构造器类</h2>
 * <p>请不要用该class声明变量</p>
 * @author Haythem
 * @since 1.0.0
 */
public class ExternalResourceBuilder {

    /**
     * <h2>外部资源构造器</h2>
     * <p>用于构造URL来源的输入，支持HTTP高级设置</p>
     */
    public ExternalResource ExtResourceBuilder(String path, HttpProperties properties){
        ExternalResource externalResource = null;
        try{
            if(path.contains("http")){//URL
                InputStream inputStream = null;
                if(properties != null){
                    inputStream = HttpUtil.getInputStream_advanced(path, properties);
                }else{
                    inputStream = HttpUtil.getInputStream(path);
                }
                externalResource = Mirai.getInstance().getFileCacheStrategy().newCache(inputStream);
            }else {//LOCAL
                File file = new File(path);
                externalResource = ExternalResource.create(file);
                FileMessageBuilder.FileName = file.getName();
            }
        }catch (IOException e){
            externalResource = null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return externalResource;
    }
}
