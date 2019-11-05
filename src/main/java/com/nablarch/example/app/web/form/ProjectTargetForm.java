package com.nablarch.example.app.web.form;

import java.io.Serializable;

import nablarch.core.log.Logger;
import nablarch.core.log.LoggerManager;
import nablarch.core.validation.ee.Required;
import nablarch.fw.dicontainer.web.RequestScoped;

/**
 * 処理対象パラメータ。
 *
 * @author Nabu Rakutaro
 */
@RequestScoped
public class ProjectTargetForm implements Serializable {

    /** ロガー **/
    private static final Logger LOGGER = LoggerManager.get(ProjectTargetForm.class);

    public ProjectTargetForm() {
        LOGGER.logDebug(getClass().getName() + " created.");
    }

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** プロジェクトID */
    @Required
    private String projectId;

    /**
     * プロジェクトIDを取得する。
     *
     * @return プロジェクトID
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * プロジェクトIDを設定する。
     *
     * @param projectId 設定するプロジェクトID
     *
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

}
