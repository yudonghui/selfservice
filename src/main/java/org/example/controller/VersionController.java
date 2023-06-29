package org.example.controller;

import org.apache.http.util.TextUtils;
import org.example.entitys.BaseBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
public class VersionController {
    @Autowired
    ResourceLoader resourceLoader;
    private final Logger logger = LoggerFactory.getLogger(VersionController.class);

    @RequestMapping(value = "/version/check", method = RequestMethod.GET)
    @CrossOrigin//解决跨域问题
    public BaseBack<Map<String, Object>> check() {
        BaseBack<Map<String, Object>> mapBaseBack = new BaseBack<>();
        mapBaseBack.setCode(1000);
        mapBaseBack.setMsg("成功");
        try {
            String fileName = "version.txt";
            //InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
            ClassPathResource resource = new ClassPathResource(fileName);
            ClassPathResource resourcef = new ClassPathResource("libreoffice");
            String path = this.getClass().getClassLoader().getResource("libreoffice").getPath();
            logger.info("路径：{} 文件路径：{},路径：{}", resource.getPath(), resourcef.getPath(), path);
            HashMap<String, Object> data = getFileContent(resource.getInputStream());
            mapBaseBack.setData(data);
        } catch (Exception e) {
            mapBaseBack.setCode(4000);
            mapBaseBack.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return mapBaseBack;
    }

    @RequestMapping(value = "/version/update", method = RequestMethod.POST)
    @CrossOrigin//解决跨域问题
    public BaseBack<Map<String, Object>> update(@RequestBody Map<String, Object> requestMap) {
        BaseBack<Map<String, Object>> mapBaseBack = new BaseBack<>();
        mapBaseBack.setCode(1000);
        mapBaseBack.setMsg("成功");
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, Object> entry : requestMap.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "\r\n");
        }
        try {
            String path = new ClassPathResource("version.txt").getURI().getPath();
            //File  file = (File) path;
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
          /*  File  file = new ClassPathResource("version.txt").getFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));*/
            bufferedWriter.write(stringBuffer.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            mapBaseBack.setCode(4000);
            mapBaseBack.setMsg(e.getMessage());
            e.printStackTrace();
        }

        /*String content = writeFile(new ClassPathResource("version.txt").getPath(), stringBuffer.toString());
        if (TextUtils.isEmpty(content)) {
            mapBaseBack.setCode(4000);
            mapBaseBack.setMsg("失败");
        }*/
        return mapBaseBack;
    }

    /**
     * 根据文件路径读取文件内容
     *
     * @param fileInPath
     * @throws IOException
     */
    public static HashMap<String, Object> getFileContent(Object fileInPath) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        BufferedReader br = null;
        if (fileInPath == null) {
            return map;
        }
        if (fileInPath instanceof String) {
            br = new BufferedReader(new FileReader(new File((String) fileInPath)));
        } else if (fileInPath instanceof InputStream) {
            br = new BufferedReader(new InputStreamReader((InputStream) fileInPath));
        }
        String line;
        while ((line = br.readLine()) != null) {
            if (!TextUtils.isEmpty(line)) {
                String[] split = line.split("=");
                map.put(split[0], split[1]);
            }
        }
        br.close();
        return map;
    }

    public static String writeFile(String path, String content) {
        //文件输出流
        FileOutputStream out = null;
        //设置文件路径
        File files = new File(path);
        if (!files.exists()) {
            // 创建文件夹
            files.mkdirs();
        }
        File file = new File(path);
        try {
            out = new FileOutputStream(file);
            //out = new FileOutputStream(file,true);//true 第二次添加会往后面追加
            out.write(content.getBytes());
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
