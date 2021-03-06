/*
 * Copyright © 2016 Liaison Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.liaison.shachi.api.request.fluid;

import com.liaison.shachi.api.request.OperationController;
import com.liaison.shachi.api.request.fluid.fluent.ColSpecReadFluent;
import com.liaison.shachi.api.request.fluid.fluent.LongValueSpecFluent;
import com.liaison.shachi.api.request.fluid.fluent.RowSpecFluent;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Specifies an <strong>HBase read operation</strong> while it is in a fluid state (i.e. wherein it
 * is still being specified  by the fluent/chaining API).
 * @author Branden Smith; Liaison Technologies, Inc.
 * @param <Z> The operation result type which will be produced when the owning
 * {@link OperationController} executes {@link OperationController#exec()}.
 */
public interface ReadOpSpecFluid<Z> extends OpSpecFluid<Z> {
    /**
     * Specify the maximum number of results to retrieve per column family.
     * @param resultsPerFamily the maximum number of results to retrieve per column family
     * @return this instance (for fluent/chaining API)
     * @throws IllegalArgumentException if resultsPerFamily is negative
     * @throws IllegalStateException if this value is already specified for this read, or if this
     * operation is not in a fluid state
     */
    ReadOpSpecFluid<Z> atMost(int resultsPerFamily) throws IllegalArgumentException, IllegalStateException;
    /**
     * Specify the range of timestamp values to which the HBase read operation specified by this
     * spec should be limited.
     * <br><br>
     * <strong>Cardinality:</strong> The timestamp range may be specified <strong>at most
     * once</strong>; implementations must throw IllegalStateException on repeated invocations of
     * this method.
     * @return a {@link LongValueSpecFluent} instance owned by this read operation specification,
     * whereby a range of timestamp values constricting the HBase read operation may be specified.
     * @throws IllegalStateException if a timestamp range is already associated with this read, or
     * if this operation is not in fluid state
     * operation specification
     */
    LongValueSpecFluent<? extends ReadOpSpecFluid<Z>> atTime() throws IllegalStateException;
    /**
     * Specify the table and row key corresponding to the HBase row from which the HBase read
     * operation specified by this spec will read.
     * <br><br>
     * <strong>Cardinality:</strong> The row specificaion must be provided <strong>exactly
     * once</strong>; implementations must throw IllegalStateException on repeated invocations of
     * this method.
     * @return a {@link RowSpecFluent} instance owned by this read operation specification, whereby
     * the table and row targeted by this read operation may be specified.
     * @throws IllegalStateException if a table+row combination is already associated with this
     * read operation (e.g. by a previous invocation of this method, or if the operation spec is
     * not in a fluid state
     * @see {@link WriteOpSpecFluid#on()} (equivalent operation on write)
     */
    RowSpecFluent<?, ? extends ReadOpSpecFluid<Z>> from() throws IllegalArgumentException, IllegalStateException;
    /**
     * Add the following column specification to the list of columns from which the HBase read
     * operation specified by this spec will read.
     * <br><br>
     * <strong>Cardinality:</strong> This method or {@link #withAllOf(Iterable, BiConsumer)} must
     * be invoked <strong>at least once</strong>, in order to specify at least one column from
     * which to read.
     * @return a {@link ColSpecReadFluent} instance owned by this read operation specification,
     * whereby the column family and qualifier from which to read may be specified.
     * @see {@link WriteOpSpecFluid#with()} (equivalent operation on write)
     */
    ColSpecReadFluent<?, ? extends ReadOpSpecFluid<Z>> with();
    /**
     * TODO: javadoc
     * @param handle
     * @return
     */
    ColSpecReadFluent<?, ? extends ReadOpSpecFluid<Z>> with(Object handle);
    /**
     * Add a series of column specifications to the list of columns from which the HBase read
     * operation specified by this spec will read. Unlike {@link #with()}, which returns a
     * {@link ColSpecReadFluent} instance allowing the column family and qualifier for each column
     * to be specified manually, this method expects a collection of arbitrarily-typed objects and
     * a two-argument consumer method/lambda, which in turn accepts an element from the list as its
     * first parameter, and an instance of {@link ColSpecReadFluid} as its second parameter. The
     * implementation must iterate through the collection (in the default iteration order specified
     * by the {@link Iterable} implementation, then provide the collection element and a new
     * instance of {@link ColSpecReadFluid} owned by this read operation specification to the
     * consumer. The client-specified consumer, then, is responsible for invoking the appropriate
     * API methods on the {@link ColSpecReadFluid} instance to transform the collection element to
     * a column specification to be added to the list of columns from which the HBase read
     * operation specified by this spec will read.
     * <br><br>
     * This method is useful in order to internalize iteration over a collection of elements which
     * indicate the columns from which the read operation should read. For example, a client with a
     * list of Strings indicating the names of column qualifiers might use this method in
     * conjunction with a lambda which transforms a String into a column family and qualifier in
     * the context of a {@link ColSpecReadFluid} instance, in order to specify that the read
     * operation should read from all of the columns in the list.
     * <br><br>
     * <strong>Cardinality:</strong> This method or {@link #with()} must be invoked <strong>at
     * least once</strong>, in order to specify at least one column from which to read.
     * @param sourceData a collection (Iterable) of elements of arbitrary type which are to be
     * transformed into column specifications ({@link ColSpecReadFluid}) indicating columns from
     * which the HBase read operation specified by this spec is to read
     * @param dataToColumnGenerator two-argument consumer method/lambda (BiConsumer) which consumes
     * an element from the sourceData collection and a new {@link ColSpecReadFluid} owned by this
     * read operation specification, then modifies the {@link ColSpecReadFluid} such that its
     * column family and qualifier are those indicated by the sourceData element
     * @return this instance (for fluent/chaining API)
     * @see {@link WriteOpSpecFluid#withAllOf(Iterable, BiConsumer)} (equivalent operation on
     * write)
     */
    <X> ReadOpSpecFluid<Z> withAllOf(Iterable<X> sourceData, BiConsumer<? super X, ColSpecReadFluid<?>> dataToColumnGenerator);
    /**
     * TODO
     * @param sourceData
     * @param dataToColumnGenerator
     * @param <X>
     * @return
     */
    <X> ReadOpSpecFluid<Z> withAllOf(Iterable<X> sourceData, BiFunction<? super X, ColSpecReadFluid<?>, Object> dataToColumnGenerator);
}
