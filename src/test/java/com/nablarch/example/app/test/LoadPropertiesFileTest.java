package com.nablarch.example.app.test;

import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;
import nablarch.core.repository.di.DiContainer;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class LoadPropertiesFileTest {

    /**
     * xml（テスト用設定ファイル）の配置ディレクトリ
     */
    private static final String COMPONENT_BASE_PATH = "com/nablarch/example/app/web/common/authentication/";

    /**
     * config-file要素のテスト。
     * <p/>
     * env.propertiesファイルの半角スペースが読み取れることの確認
     */
    @Test
    public void testLoadPropertiesFile() {

        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(
                COMPONENT_BASE_PATH + "authentication-db.xml");
        DiContainer container = new DiContainer(loader);

        String value1 = container.getComponentByName("nablarch.onebytespace");

        assertThat(value1, is(" ")); // nablarch.onebytespaceの内容が読み込まれ半角として扱えること
    }

}
