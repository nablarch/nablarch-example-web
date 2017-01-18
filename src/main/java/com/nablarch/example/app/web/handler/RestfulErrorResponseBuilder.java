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
import java.util.List;
import java.util.stream.Collectors;

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
            final List<ErrorMessage> errorMessages = ((ApplicationException) throwable).getMessages()
                    .stream()
                    .map(message ->
                        message instanceof ValidationResultMessage
                                ? new ErrorMessage(message.formatMessage(), ((ValidationResultMessage) message).getPropertyName())
                                : new ErrorMessage(message.formatMessage())
                    )
                    .collect(Collectors.toList());

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

        /**
         * エラーメッセージを保持するインスタンスを生成する。
         *
         * @param message エラーメッセージ
         */
        ErrorMessage(final String message) {
            this(message, null);
        }

        /**
         * エラーメッセージとプロパティ名を保持するインスタンスを生成する。
         *
         * @param message エラーメッセージ
         * @param propertyName プロパティ名
         */
        ErrorMessage(final String message, final String propertyName) {
            this.message = message;
            this.propertyName = propertyName;
        }

        /** プロパティ名 */
        private final String propertyName;

        /** エラーメッセージ */
        private final String message;

        /**
         * プロパティ名を取得する。
         *
         * @return プロパティ名
         */
        public String getPropertyName() {
            return propertyName;
        }

        /**
         * エラーメッセージを取得する。
         *
         * @return エラーメッセージ
         */
        public String getMessage() {
            return message;
        }
    }
}
