/**
 * Copyright 2015 Liaison Technologies, Inc.
 * This software is the confidential and proprietary information of
 * Liaison Technologies, Inc. ("Confidential Information").  You shall
 * not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Liaison Technologies.
 */
package com.liaison.hbase.api.request.fluid;

import com.liaison.hbase.dto.RowKey;
import com.liaison.hbase.model.TableModel;

/**
 * TODO
 * @author Branden Smith; Liaison Technologies, Inc.
 */
public interface RowSpecFluid<R extends RowSpecFluid<R>> {
    R tbl(final TableModel table) throws IllegalStateException, IllegalArgumentException;
    R row(final RowKey rowKey) throws IllegalStateException, IllegalArgumentException;
}