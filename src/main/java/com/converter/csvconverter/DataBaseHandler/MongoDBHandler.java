package com.converter.csvconverter.DataBaseHandler;

import com.converter.csvconverter.Utilities.ObjSizeCalculator;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertManyOptions;
import com.opencsv.CSVReader;

import org.bson.Document;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class MongoDBHandler implements DataBaseHandler {

    private MongoClient mongoClient;
    private String DB_NAME;
    private String DOC_NAME;

    public CSVReader READER;
    public MongoDBHandler() {
    }

    public MongoDBHandler(MongoClient mongoClient, String DB_NAME, String DOC_NAME) {
        this.mongoClient = mongoClient;
        this.DB_NAME = DB_NAME;
        this.DOC_NAME = DOC_NAME;
    }
    @Override
    public String ddlBuilder(CSVReader reader) throws Exception {
        if (reader == null) throw new Exception("CSV Reader ERROR!");
        return null;
    }

    @Override
    public String batchInsertBuilder(CSVReader reader) throws Exception {
        this.READER = reader;
        return null;
    }

    public List<List<Document>> chunkDocuments(CSVReader reader, long SIZE, String[] headers) throws Exception {
        assert reader != null;
        assert headers != null;
        List<List<Document>> batches = new ArrayList<>();
        List<Document> current_batch = new ArrayList<>();
        long current_batch_size = 0;

        String[] row;
        System.out.println("Chunker initialized");

        while ((row = reader.readNext()) != null) {
            Document document = new Document();
            if (current_batch_size < SIZE) {
                for (int i = 0; i < headers.length; i++) {
                    document.append(headers[i], row[i]);
                    System.out.println(headers[i] + row[i]);
                }
                current_batch.add(document);
                current_batch_size += 1;
            } else {
                System.out.println("UPDATE: Chunking large file");
                batches.add(current_batch);
                current_batch.clear();
                current_batch_size = 0;
            }
        }
        batches.add(current_batch);
        System.out.println("Done chunking");
        return batches;
    }

    @Override
    public void createTable(String ddl) throws Exception {
        if (ddl != null) throw new Exception();
    }

    @Override
    public void uploadToDB(String ps) throws Exception{
        System.out.println("Initialising upload to MongoDB");
        try {
            String[] headers = this.READER.readNext();
            List<List<Document>> batches = chunkDocuments(this.READER, 1024*1024*10, headers);
            System.out.println(batches.toString());
            for (List<Document> batch : batches) {
                commitBatch(batch);
            }
            mongoClient.close();
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception("CSV READER ERROR");
        }
    }

    public void uploadToDB(CSVReader reader) throws Exception {
        System.out.println("Initialising upload to MongoDB");

        try {
            String[] headers = reader.readNext();
            List<List<Document>> batches = chunkDocuments(reader, 1024*1024*10, headers);
            System.out.println(batches.toString());
            for (List<Document> batch : batches) {
                commitBatch(batch);
            }
            mongoClient.close();
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception("CSV READER ERROR");
        }
    }

    public void commitBatch(List<Document> batch) {
        System.out.println("Works");
        assert batch != null;
        MongoDatabase database = mongoClient.getDatabase(this.DOC_NAME);
        MongoCollection<Document> collection = database.getCollection(this.DB_NAME);
        collection.insertMany(batch, new InsertManyOptions().ordered(false));
    }

}
