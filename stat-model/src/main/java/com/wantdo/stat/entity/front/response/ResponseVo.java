package com.wantdo.stat.entity.front.response;

import com.wantdo.stat.entity.base.BaseModel;

/**
 * @Date : 2015-9-17
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class ResponseVo extends BaseModel {
    private static final long serialVersionUID = -3428260061627238323L;

    private int result = ResponseVoResultCode.CODE_SUCCESS;
    private String message;
    private Object data;

    public static ResponseVo newInstance(){
        return new ResponseVo();
    }

    public ResponseVo(){}

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
