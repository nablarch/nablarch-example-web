package com.nablarch.example.app.web.form;

import javax.validation.Valid;

import nablarch.core.log.Logger;
import nablarch.core.log.LoggerManager;
import nablarch.fw.dicontainer.web.RequestScoped;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * プロジェクト一括更新フォーム
 *
 * @author Nabu Rakutaro
 */
@RequestScoped
public class ProjectBulkForm implements Serializable {

    /** ロガー **/
    private static final Logger LOGGER = LoggerManager.get(ProjectBulkForm.class);

    public ProjectBulkForm() {
        LOGGER.logDebug(getClass().getName() + " created.");
    }

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** プロジェクト情報のリスト */
    @Valid
    private ArrayList<InnerProjectForm> projectList = new ArrayList<>();

    /**
     * プロジェクト情報のリストを返す。
     *
     * @return プロジェクト情報のリスト
     */
    public ArrayList<InnerProjectForm> getProjectList() {
        return projectList;
    }

    /**
     * プロジェクト情報のリストを設定する。
     *
     * @param projectList 設定したいプロジェクト情報のリスト
     */
    public void setProjectList(ArrayList<InnerProjectForm> projectList) {
        this.projectList = projectList;
    }
}
