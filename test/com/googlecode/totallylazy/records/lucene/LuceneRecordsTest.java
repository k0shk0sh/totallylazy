package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.records.AbstractRecordsTests;
import com.googlecode.totallylazy.records.Records;
import com.googlecode.totallylazy.records.lucene.mappings.Mappings;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static com.googlecode.totallylazy.Files.temporaryDirectory;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;

public class LuceneRecordsTest extends AbstractRecordsTests {

    public static final Version VERSION = Version.LUCENE_33;
    public static final Analyzer ANALYZER = new StandardAnalyzer(VERSION);
    private Directory directory;
    private IndexWriter writer;

    @Override
    protected Records createRecords() throws Exception {
        File path = temporaryDirectory();
        System.out.println("path = " + path);
        directory = new NIOFSDirectory(path);
        writer = new IndexWriter(directory, new IndexWriterConfig(VERSION, ANALYZER));
        return new LuceneRecords(directory, writer, new Mappings(), System.out);
    }

    @After
    public void cleanUp() throws IOException {
        writer.close();
        directory.close();
    }

    @Test
    public void canQueryIndexDirectly() throws Exception {
        QueryParser parser = new QueryParser(VERSION, null, ANALYZER);
        assertThat(((LuceneRecords) records).query(parser.parse("type:people +firstName:da*")).map(lastName), hasExactly("bodart"));
    }

    @Override
    @Ignore("Not working yet")
    public void supportsUri() throws Exception {
    }

    @Override
    @Ignore("Not working yet")
    public void supportsJoin() throws Exception {
    }
}
