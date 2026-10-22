import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Task #3 サンプリング比較用: .xls / .xlsx の全シートの非空セルを
 * 「シート名 TAB 行(1始まり) TAB 列(1始まり) TAB 値」の TSV で標準出力へダンプする。
 * 値中の改行は \n、タブは \t にエスケープする。空シートも検出できるよう、各シート先頭に
 * 「シート名 TAB 0 TAB 0 TAB __SHEET__」の行を出力する。
 * 実行: java -cp poi-3.8.jar:poi-ooxml-3.8.jar:poi-ooxml-schemas-3.8.jar:xmlbeans-2.3.0.jar:dom4j-1.6.1.jar
 *       XlsDump.java <file.xls|file.xlsx>
 */
public class XlsDump {
    public static void main(String[] args) throws Exception {
        DataFormatter fmt = new DataFormatter();
        try (InputStream in = new FileInputStream(args[0])) {
            Workbook wb = WorkbookFactory.create(in);
            for (int s = 0; s < wb.getNumberOfSheets(); s++) {
                Sheet sheet = wb.getSheetAt(s);
                System.out.println(sheet.getSheetName() + "\t0\t0\t__SHEET__");
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        String v = fmt.formatCellValue(cell);
                        if (v == null || v.isEmpty()) {
                            continue;
                        }
                        v = v.replace("\\", "\\\\").replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t");
                        System.out.println(sheet.getSheetName() + "\t" + (row.getRowNum() + 1) + "\t"
                                + (cell.getColumnIndex() + 1) + "\t" + v);
                    }
                }
            }
        }
    }
}
