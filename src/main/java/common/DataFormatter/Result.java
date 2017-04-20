package common.DataFormatter;

import java.io.Serializable;

public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    // 成功的状态码
    public static final int SUCCESS = 0;
    // 返回的状态码
    private int code;
    // 返回的核心数据
    private T data;

    // 默认构造器
    public Result() {

    }

    // 只返回状态码的构造器
    public Result(int code) {
        this.code = code;
        this.data = null;
    }

    // 返回状态码及核心数据的构造器
    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }

    // 创建成功的返回对象，返回状态码
    public static <T> Result<T> SUCCESS() {
        return new Result<T>(SUCCESS);
    }

    // 创建成功的返回对象，返回状态码及核心数据
    public static <T> Result<T> SUCCESS(T data) {
        return new Result<T>(SUCCESS, data);
    }

    // 创建错误的返回对象，返回状态码
    public static <T> Result<T> ERROR(int code) {
        return new Result<T>(code);
    }

    // 创建错误的返回对象，返回状态码及错误提示
    public static <T> Result<T> ERROR(int code, T data) {
        return new Result<T>(code, data);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
