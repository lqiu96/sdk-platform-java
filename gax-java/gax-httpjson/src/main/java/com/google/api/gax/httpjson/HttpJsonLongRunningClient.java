/*
 * Copyright 2021 Google LLC
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *     * Neither the name of Google LLC nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.google.api.gax.httpjson;

import com.google.api.core.ApiFunction;
import com.google.api.core.InternalApi;
import com.google.api.gax.longrunning.OperationSnapshot;
import com.google.api.gax.rpc.LongRunningClient;
import com.google.api.gax.rpc.TranslatingUnaryCallable;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.longrunning.CancelOperationRequest;
import com.google.longrunning.DeleteOperationRequest;
import com.google.longrunning.GetOperationRequest;
import com.google.longrunning.Operation;
import com.google.protobuf.Empty;

/**
 * Implementation of LongRunningClient based on REST transport.
 *
 * <p>Public for technical reasons. For internal use only.
 */
@InternalApi
public class HttpJsonLongRunningClient implements LongRunningClient {

  private final UnaryCallable getOperationCallable;
  private final UnaryCallable cancelOperationCallable;
  private final UnaryCallable deleteOperationCallable;

  private HttpJsonLongRunningClient(
      UnaryCallable getOperationCallable,
      UnaryCallable cancelOperationCallable,
      UnaryCallable deleteOperationCallable) {
    this.getOperationCallable = getOperationCallable;
    this.cancelOperationCallable = cancelOperationCallable;
    this.deleteOperationCallable = deleteOperationCallable;
  }

  //  private final UnaryCallable<RequestT, OperationT> operationCallable;
  //  private final OperationSnapshotFactory<RequestT, OperationT> operationSnapshotFactory;
  //  private final PollingRequestFactory<RequestT> pollingRequestFactory;

  //  public HttpJsonLongRunningClient(
  //      UnaryCallable<RequestT, OperationT> operationCallable,
  //      OperationSnapshotFactory<RequestT, OperationT> operationSnapshotFactory,
  //      PollingRequestFactory<RequestT> pollingRequestFactory) {
  //    this.operationCallable = operationCallable;
  //    this.operationSnapshotFactory = operationSnapshotFactory;
  //    this.pollingRequestFactory = pollingRequestFactory;
  //  }

  /** {@inheritDoc} */
  @Override
  public UnaryCallable<String, OperationSnapshot> getOperationCallable() {
    return TranslatingUnaryCallable.create(
        getOperationCallable,
        new ApiFunction<String, GetOperationRequest>() {
          @Override
          public GetOperationRequest apply(String request) {
            return GetOperationRequest.newBuilder().setName(request).build();
          }
        },
        new ApiFunction<Operation, OperationSnapshot>() {
          @Override
          public OperationSnapshot apply(Operation operation) {
            return HttpJsonOperationSnapshot.create(operation);
          }
        });
  }

  /** {@inheritDoc} */
  @Override
  public UnaryCallable<String, Void> cancelOperationCallable() {
    return TranslatingUnaryCallable.create(
        cancelOperationCallable,
        new ApiFunction<String, CancelOperationRequest>() {
          @Override
          public CancelOperationRequest apply(String request) {
            return CancelOperationRequest.newBuilder().setName(request).build();
          }
        },
        new ApiFunction<Empty, Void>() {
          @Override
          public Void apply(Empty empty) {
            return null;
          }
        });
  }

  /** {@inheritDoc} */
  @Override
  public UnaryCallable<String, Void> deleteOperationCallable() {
    return TranslatingUnaryCallable.create(
        deleteOperationCallable,
        new ApiFunction<String, DeleteOperationRequest>() {
          @Override
          public DeleteOperationRequest apply(String request) {
            return DeleteOperationRequest.newBuilder().setName(request).build();
          }
        },
        new ApiFunction<Empty, Void>() {
          @Override
          public Void apply(Empty empty) {
            return null;
          }
        });
  }

  public static HttpJsonLongRunningClient create(
      UnaryCallable getOperationCallable,
      UnaryCallable cancelOperationCallable,
      UnaryCallable deleteOperationCallable) {
    return new HttpJsonLongRunningClient(
        getOperationCallable, cancelOperationCallable, deleteOperationCallable);
  }
}
