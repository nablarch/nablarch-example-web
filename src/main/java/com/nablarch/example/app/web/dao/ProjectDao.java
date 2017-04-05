package com.nablarch.example.app.web.dao;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.FetchType;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.SelectOptions;

import nablarch.common.dao.EntityList;
import nablarch.integration.doma.DomaConfig;

import com.nablarch.example.app.entity.Project;
import com.nablarch.example.app.web.dto.ProjectDownloadDto;
import com.nablarch.example.app.web.dto.ProjectDto;
import com.nablarch.example.app.web.dto.ProjectSearchDto;

/**
 * プロジェクトDao。
 *
 * @author Nabu Rakutaro
 */
@Dao(config = DomaConfig.class)
public interface ProjectDao {

    /**
     * プロジェクトを取得する。
     *
     * @param id ID
     * @return プロジェクト
     */
    @Select(ensureResult = true)
    Project findById(Integer id);

    /**
     * IDとユーザIDに紐づくプロジェクトを取得する。
     *
     * @param id ID
     * @param userId ユーザID
     * @return プロジェクト
     */
    @Select(ensureResult = true)
    ProjectDto findByIdAndUserId(Integer id, Integer userId);

    /**
     * プロジェクト情報を検索する。
     *
     * @param condition 条件
     * @param selectOptions 検索オプション
     * @return 検索結果
     */
    @Select(fetchSize = 50)
    List<Project> searchProject(ProjectSearchDto condition, SelectOptions selectOptions);

    /**
     * プロジェクト情報を検索する。
     *
     * @param condition 条件
     * @param page ページ番号
     * @param max 取得最大件数
     * @return 検索結果
     */
    default ProjectSearchResult searchProject(ProjectSearchDto condition, int page, int max) {
        final SelectOptions options = SelectOptions.get()
                                                   .offset(page == 1 ? 0 : (page - 1) * max)
                                                   .limit(20)
                                                   .count();

        final List<Project> projects = searchProject(condition, options);
        return new ProjectSearchResult(projects, page, max, options.getCount());
    }

    /**
     * ダウンロードするプロジェクトを検索する。
     *
     * @param condition 条件
     * @param function 検索結果を処理する関数
     * @return 検索結果を処理した結果
     */
    @Select(fetch = FetchType.LAZY, strategy = SelectType.STREAM)
    <T> T searchProjectToDownload(ProjectSearchDto condition, Function<Stream<ProjectDownloadDto>, T> function);

    /**
     * プロジェクトを更新する。
     *
     * @param project プロジェクト
     * @return 更新件数
     */
    @Update
    int update(Project project);

    /**
     * プロジェクトを一括更新する。
     * @param projects 更新対象のプロジェクトリスト
     * @return 更新件数
     */
    @BatchUpdate
    int[] bulkUpdate(List<Project> projects);

    /**
     * プロジェクトを削除する。
     *
     * @param project プロジェクト
     * @return 削除件数
     */
    @Delete(ignoreVersion = true)
    int delete(Project project);

    /**
     * プロジェクトを登録する。
     *
     * @param project プロジェクト
     * @return 登録件数
     */
    @Insert
    int insert(Project project);

    /**
     * プロジェクトを一括登録する。
     * @param projects 登録対象のプロジェクトリスト
     * @return 登録件数
     */
    @BatchInsert(batchSize = 100)
    int[] bulkInsert(List<Project> projects);

    /**
     * プロジェクト検索結果を保持するクラス。
     */
    class ProjectSearchResult extends EntityList<Project> {

        /**
         * プロジェクト検索結果を構築する。
         * @param c 検索結果。
         * @param page ページ番号
         * @param max 最大取得件数
         * @param resultCount 検索条件に一致するレコード数
         */
        public ProjectSearchResult(final Collection<? extends Project> c, long page, long max, long resultCount) {
            super(c);
            setMax(max);
            setPage(page);
            setResultCount(resultCount);
        }
    }
}
