package com.clarity.binary.parse;

import java.util.ArrayList;
import java.util.List;

import com.clarity.parser.RawFile;

/**
 * Represents a collection of source files.
 */
public class CodeBase {

    private List<RawFile> sourceFiles = new ArrayList<RawFile>();

    public List<RawFile> getSourceFiles() {
        return sourceFiles;
    }

    public String getFileContents(final String fileName) {
        for (final RawFile file : sourceFiles) {
            if (file.name().equals(fileName)) {
                return file.content();
            }
        }
        return "Clarity could not retrieve the contents of that file.";
    }

    public void insertFile(final RawFile rawFile) {
        sourceFiles.add(rawFile);
    }

    public void setFiles(final List<RawFile> files) {
        sourceFiles = files;
    }

    public CodeBase() {
    }
}
