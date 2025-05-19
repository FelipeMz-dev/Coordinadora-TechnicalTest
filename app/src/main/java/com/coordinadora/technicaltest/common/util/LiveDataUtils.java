package com.coordinadora.technicaltest.common.util;

import android.view.View;

public class LiveDataUtils {

    public interface SuccessCallback<T> {
        void onSuccess(T data);
    }

    public interface ErrorCallback {
        void onError(String message);
    }


    public static <T> void handleResponse(
            ResponseState<T> response,
            View loadingOverlay,
            SuccessCallback<T> onSuccess,
            ErrorCallback onError
    ) {
        switch (response.status) {
            case LOADING:
                loadingOverlay.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                loadingOverlay.setVisibility(View.GONE);
                onSuccess.onSuccess(response.data);
                break;
            case ERROR:
                loadingOverlay.setVisibility(View.GONE);
                onError.onError(response.message);
                break;
        }
    }
}
