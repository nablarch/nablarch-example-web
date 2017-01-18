package com.nablarch.example.app.web.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nablarch.core.message.ApplicationException;
import nablarch.core.validation.ValidationResultMessage;
import nablarch.fw.ExecutionContext;
import nablarch.fw.jaxrs.ErrorResponseBuilder;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.HttpResponse;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * RESTful API用のエラーレスポンス生成クラス。
 *
 * @author Nabu Rakutaro
 */
public class RestfulErrorResponseBuilder extends ErrorResponseBuilder {

    /**
     * エラーレスポンスを生成する。
     * <p/>
     * 発生したエラーがバリデーションエラーの場合、
     * 対応するエラーメッセージ及びプロパティ名をJSON形式でレスポンスに設定する。
     * <p/>
     * それ以外の{@link ApplicationException}の場合は、対応するエラーメッセージをJSON形式でレスポンスに設定する。
     * <p/>
     * それ以外のエラーの場合には、{@link ErrorResponseBuilder#build(HttpRequest, ExecutionContext, Throwable)}に処理を委譲する。
     *
     * @param request HTTPリクエスト
     * @param context 実行コンテキスト
     * @param throwable 発生したエラーの情報
     * @return エラーレスポンス
     */
    @Override
    public HttpResponse build(HttpRequest request, ExecutionContext context, Throwable throwable) {
        if (throwable instanceof ApplicationException) {
            final HttpResponse response = new HttpResponse(400);
            response.setContentType(MediaType.APPLICATION_JSON);
            final List<ErrorMessage> errorMessages = new ArrayList<>();
            ((ApplicationException) throwable).getMessages().forEach(message -> {
                final ErrorMessage errorMessage = new ErrorMessage();
                    if (message instanceof ValidationResultMessage) {
                        errorMessage.setPropertyName(((ValidationResultMessage) message).getPropertyName());
                    }
                    errorMessage.setMessage(message.formatMessage());
                    errorMessages.add(errorMessage);
            });

            try {
                response.write(new ObjectMapper().writeValueAsString(errorMessages));
            } catch (JsonProcessingException e) {
                return new HttpResponse(500);
            }
            return response;
        } else {
            return super.build(request, context, throwable);
        }
    }

    /**
     * エラーメッセージを保持するクラス。
     */
    private static class ErrorMessage {

        /** プロパティ名 */
        private String propertyName;

        /** エラーメッセージ */
        private String message;

        /**
         * プロパティ名を取得する。
         *
         * @return プロパティ名
         */
        public String getPropertyName() {
            return propertyName;
        }

        /**
         * プロパティ名を設定する。
         *
         * @param propertyName プロパティ名
         */
        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        /**
         * エラーメッセージを取得する。
         *
         * @return エラーメッセージ
         */
        public String getMessage() {
            return message;
        }

        /**
         * エラーメッセージを設定する。
         *
         * @param message エラーメッセージ
         */
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
