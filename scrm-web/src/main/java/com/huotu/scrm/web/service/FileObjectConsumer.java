package com.huotu.scrm.web.service;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import java.util.Objects;

/**
 * @author CJ
 */
@FunctionalInterface
public interface FileObjectConsumer {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(FileObject t) throws FileSystemException;

    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code Consumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default FileObjectConsumer andThen(FileObjectConsumer after) {
        Objects.requireNonNull(after);
        return t -> { accept(t); after.accept(t); };
    }

}
