package src.models.validators;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import src.models.Employee;
import src.utils.DBUtil;

public class EmployeeValidator {
    // 新規登録と更新の際、入力必須・重複チェックを行うメソッド　引数は従業員インスタンスと社員番号重複チェックフラグ、パスワードチェック必要性有無フラグ
    public static List<String> validate(Employee e, Boolean code_duplicate_check_flag, Boolean password_check_flag) {

        // エラーリストを宣言
        List<String> errors = new ArrayList<String>();

        // 社員番号エラーメッセージを宣言　必須入力と重複に関するバリデートメソッド（従業員インスタンスの社員番号フィールド、社員番号チェック必要性フラグ）
        String code_error = _validateCode(e.getCode(), code_duplicate_check_flag);
        // 社員番号エラーメッセージがバリデートメソッドの戻り値として格納されている＝＞エラーリストに社員番号エラーメッセージを追加
        if(!code_error.equals("")) {
            errors.add(code_error);
        }

        // 名前エラーメッセージを宣言　必須入力に関するバリデートメソッド（従業員インスタンスの名前フィールド）
        String name_error = _validateName(e.getName());
        // 名前エラーメッセージがバリデートメソッドの戻り値として格納されている＝＞エラーリストに名前エラーメッセージを追加
        if(!name_error.equals("")) {
            errors.add(name_error);
        }

        // パスワードエラーメッセージを宣言　必須入力に関するバリでーどメソッド（従業員インスタンスのパスワードフィールド、パスワードチェック必要性有無フラグ）
        String password_error = _validatePassword(e.getPassword(), password_check_flag);
        // パスワードエラーメッセージがバリデートメソッドの戻り値として格納されている＝＞エラーリストにパスワードエラーメッセージを追加
        if(!password_error.equals("")) {
            errors.add(password_error);
        }

        // エラーリストを返す
        return errors;
    }

    // 社員番号　社員番号と重複チェックフラグを渡すと、エラーメッセージを返してくれるメソッド
    private static String _validateCode(String code, Boolean code_duplicate_check_flag) {
        // 必須入力チェック
        if(code == null || code.equals("")) {
            return "社員番号を入力してください。";
        }

        // すでに登録されている社員番号との重複チェック
        if(code_duplicate_check_flag) {
            EntityManager em = DBUtil.createEntityManager();
            long employees_count = (long)em.createNamedQuery("checkRegisteredCode", Long.class)
                                           .setParameter("code", code)
                                             .getSingleResult();
            em.close();
            if(employees_count > 0) {
                return "入力された社員番号の情報はすでに存在しています。";
            }
        }

        return "";
    }

    // 社員名の必須入力チェック
    private static String _validateName(String name) {
        if(name == null || name.equals("")) {
            return "氏名を入力してください。";
        }

        return "";
    }

    // パスワードの必須入力チェック
    private static String _validatePassword(String password, Boolean password_check_flag) {
        // パスワードを変更する場合のみ実行
        if(password_check_flag && (password == null || password.equals(""))) {
            return "パスワードを入力してください。";
        }
        return "";
    }
}