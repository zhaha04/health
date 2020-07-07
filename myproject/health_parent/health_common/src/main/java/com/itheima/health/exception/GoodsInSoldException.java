package com.itheima.health.exception;

/**
 * @author 张鹏
 * @date 2020/7/6 17:21
 */

/**
 * 商品在售异常
 */
public class GoodsInSoldException extends RuntimeException {

    public GoodsInSoldException() {
    }

    public GoodsInSoldException(String message) {
        super(message);
    }
}
