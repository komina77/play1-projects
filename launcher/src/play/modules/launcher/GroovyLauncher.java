package play.modules.launcher;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.groovy.runtime.InvokerHelper;

import groovy.lang.Binding;
import groovy.lang.Script;
import play.Logger;
import play.Play;
import play.PlayPlugin;
import play.i18n.Lang;
import play.i18n.Messages;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.templates.BaseTemplate;
import play.templates.GroovyTemplate;
import play.vfs.VirtualFile;

/**
 * Groovyスクリプトを実行するための補助クラス.
 * Playframeworkの持つGroovyを用いたテンプレートの仕組みの力を借りてファイルへのキャッシュ機能を実現している。
 * precompileコマンド非対応。
 */
public class GroovyLauncher extends PlayPlugin {
    /**
     * TODO: Groovy scriptのプリコンパイル.
     */
    @Override
    public boolean compileSources() {
        // TODO 自動生成されたメソッド・スタブ
        return super.compileSources();
    }

    /**
     * TODO: WEBサーバ経由の実行機能
     */
    @Override
    public boolean rawInvocation(Request request, Response response) throws Exception {
        // TODO 自動生成されたメソッド・スタブ
        return super.rawInvocation(request, response);
    }

    public BaseTemplate template;
    Script script;
    Map<String, Object> params = new HashMap<String, Object>();

    public GroovyLauncher compile(VirtualFile file) {
        template = new GroovyTemplate(file.relativePath(), file.contentAsString());
        if (!template.loadFromCache()) {
            template.compiledSource = template.source;
        }
        template.compile();

        return this;
    }

    /**
     * バインド変数設定
     * @param key 変数名
     * @param value オブジェクト
     * @return
     */
    public GroovyLauncher setParameter(String key, Object value) {
        params.put(key, value);
        return this;
    }

    /**
     * バインド変数まとめて設定
     * @param args
     * @return
     */
    public GroovyLauncher setParameters(Map<String, Object> args) {
        params.putAll(args);
        return this;
    }

    /**
     * スクリプト生成まで
     * @return
     */
    public GroovyLauncher createScript() {
        if (template == null) {
            Logger.error("Should be called after compile().");
            return this;
        }
        Binding binding = new Binding(params);
        binding.setVariable("play", new Play());
        binding.setVariable("messages", new Messages());
        binding.setVariable("lang", Lang.get());

        // If current response-object is present, add _response_encoding'
        Http.Response currentResponse = Http.Response.current();
        if (currentResponse != null) {
            binding.setVariable("_response_encoding", currentResponse.encoding);
        }

        script = InvokerHelper.createScript(template.compiledTemplate, binding);
        return this;
    }

    /**
     * スクリプト実行
     * @return 実行結果
     */
    public Object executeScript() {
        createScript();
        return script.run();
    }

    /**
     * スクリプト実行
     * @return 実行結果
     * @throws ClassNotFoundException
     */
    public Object executeScript(String name) throws ClassNotFoundException {
        if (template == null) {
            Logger.error("Should be called after compile().");
            return this;
        }
        template.compiledTemplate = template.compiledTemplate.getClassLoader().loadClass(name);
        template.compiledTemplateName = name;
        createScript();
        return script.run();
    }

}
