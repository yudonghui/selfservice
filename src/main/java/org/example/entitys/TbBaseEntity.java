package org.example.entitys;

/**
 * Created by ydh on 2022/8/23
 */
public class TbBaseEntity<T> {
    private String is_default;
    private String request_id;
    private int code;
    private T result_list;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public T getResult_list() {
        return result_list;
    }

    public void setResult_list(T result_list) {
        this.result_list = result_list;
    }
}
