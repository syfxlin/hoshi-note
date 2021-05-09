package me.ixk.hoshi.common.exception;

/**
 * @author Otstar Lin
 * @date 2021/5/9 下午 3:39
 */
public class UnsupportedInstantiationException extends Exception {

    private static final long serialVersionUID = 1028147039710816203L;

    public UnsupportedInstantiationException(final Class<?> target) {
        super(target.getName());
    }
}
