/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package ai.djl.dlr.engine;

import ai.djl.Device;
import ai.djl.ndarray.BaseNDManager;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

/** {@code DlrNDManager} is the DLR implementation of {@link NDManager}. */
public class DlrNDManager extends BaseNDManager {

    private static final DlrNDManager SYSTEM_MANAGER = new SystemManager();

    private DlrNDManager(NDManager parent, Device device) {
        super(parent, device);
    }

    static DlrNDManager getSystemManager() {
        return SYSTEM_MANAGER;
    }

    /** {@inheritDoc} */
    @Override
    public DlrNDArray create(Buffer data, Shape shape, DataType dataType) {
        if (dataType != DataType.FLOAT32 && !(data instanceof FloatBuffer)) {
            throw new UnsupportedOperationException("DLR only supports float32");
        }
        return new DlrNDArray(this, (FloatBuffer) data, shape);
    }

    /** {@inheritDoc} */
    @Override
    public NDArray zeros(Shape shape, DataType dataType) {
        if (dataType != DataType.FLOAT32) {
            throw new UnsupportedOperationException("DLR only supports float32");
        }
        int size = (int) shape.size();
        float[] data = new float[size];
        return new DlrNDArray(this, FloatBuffer.wrap(data), shape);
    }

    /** {@inheritDoc} */
    @Override
    public NDArray ones(Shape shape, DataType dataType) {
        if (dataType != DataType.FLOAT32) {
            throw new UnsupportedOperationException("DLR only supports float32");
        }
        int size = (int) shape.size();
        float[] data = new float[size];
        Arrays.fill(data, 1f);
        return new DlrNDArray(this, FloatBuffer.wrap(data), shape);
    }

    /** The SystemManager is the root {@link DlrNDManager} of which all others are children. */
    private static final class SystemManager extends DlrNDManager {

        SystemManager() {
            super(null, null);
        }

        /** {@inheritDoc} */
        @Override
        public void attach(String resourceId, AutoCloseable resource) {}

        /** {@inheritDoc} */
        @Override
        public void detach(String resourceId) {}

        /** {@inheritDoc} */
        @Override
        public void close() {}
    }
}
