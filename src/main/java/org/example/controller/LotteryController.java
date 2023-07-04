package org.example.controller;

import org.example.commons.Constants;
import org.example.networks.HttpClientUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class LotteryController {
    @RequestMapping("/ssqList")
    @CrossOrigin//解决跨域问题
    public String ssqList(String issueCount, int pageNo, int pageSize) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("name", "ssq");
        paramMap.put("issueCount", issueCount);
        paramMap.put("pageNo", pageNo + "");
        paramMap.put("pageSize", pageSize + "");
        paramMap.put("systemType", "PC");
        String request = HttpClientUtil.getRequest(Constants.SSQ_HIS_URL, paramMap);
        return request;
    }
}
