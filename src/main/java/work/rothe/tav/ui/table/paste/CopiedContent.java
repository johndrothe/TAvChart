package work.rothe.tav.ui.table.paste;

import work.rothe.tav.ui.table.paste.enums.ContentShape;

import java.util.Arrays;
import java.util.List;

import static java.util.function.Predicate.not;

public class CopiedContent {
    private static final String LF = "\n";
    private static final String TAB = "\t";

    private final List<Row> rows;
    private final int dataColumnCount;
    private final ContentShape shape;

    private CopiedContent(String content, int tableColumnCount) {
        this.rows = toRows(content);
        this.dataColumnCount = countColumns(rows);
        this.shape = ContentShape.of(this, tableColumnCount);
    }

    public static CopiedContent of(Object content, int tableColumnCount) {
        return new CopiedContent((String) content, tableColumnCount);
    }

    public static CopiedContent of(String content, int tableColumnCount) {
        return new CopiedContent(content, tableColumnCount);
    }

    public ContentShape shape() {
        return shape;
    }

    public int getColumnCount() {
        return dataColumnCount;
    }

    public int getRowCount() {
        return rows.size();
    }

    public boolean isEmpty() {
        return getRowCount() == 0;
    }

    public String getValueAt(int row, int column) {
        if (row < 0 || row >= getRowCount()) {
            throw new IndexOutOfBoundsException("Row out of bounds: " + row);
        }
        if (column < 0 || column >= getColumnCount()) {
            throw new IndexOutOfBoundsException("Column out of bounds: " + column);
        }
        return rows.get(row).get(column);
    }

    private static int countColumns(List<Row> rows) {
        return rows.stream().mapToInt(Row::size).min().orElse(0);
    }

    private static List<Row> toRows(String data) {
        return Arrays.stream(data.split(LF + "+"))
                .filter(not(String::isEmpty))
                .map(line -> line.split(TAB))
                .map(Row::new)
                .filter(not(Row::isEmpty))
                .toList();
    }

    private record Row(String[] columns) {
        public int size() {
            return columns.length;
        }

        public boolean isEmpty() {
            return columns.length == 0 || (columns.length == 1 && columns[0].isEmpty());
        }

        public String get(int index) {
            return columns[index];
        }
    }
}
