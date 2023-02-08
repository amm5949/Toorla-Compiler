package compiler;

import java.util.Hashtable;
import java.util.Map;

public class SymbolTable {
    private String name = "";
    private int scopeNumber = 0;

    public Map<String, String> symbolTable;
    public SymbolTable() {
        symbolTable = new Hashtable<String, String>();
    }

    public void setNameAndScopeNumber(String name, int scopeNumber) {
        this.name = name;
        this.scopeNumber = scopeNumber;
    }
    public void makeHashTable(String key, String value) {
        this.symbolTable.put(key, value);
    }

    public String getName() {
        return this.name;
    }
    public String printItems() {
        String itemStr = "";
        for (Map.Entry<String, String> entry: this.symbolTable.entrySet()) {
            itemStr += "Key: " + entry.getKey() + " | Value = " + entry.getValue() + "\n";
        }
        return itemStr;
    }

    public int getScopeNumber() {
        return scopeNumber;
    }

    public String toString() {
        return "------------- " + name + " : " + scopeNumber + " -------------\n" +
                printItems() +
                "-----------------------------------------\n";
    }
}
