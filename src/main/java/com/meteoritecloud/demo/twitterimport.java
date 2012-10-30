package com.meteoritecloud.demo;

import java.util.Date;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.RowProducer;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

import twitter4j.Status;

public class twitterimport {
	Trans trans;
	RowProducer rp;
	RowMetaInterface rowMeta;
	int i = 0;
	public twitterimport() throws KettleException{
		System.out.println("constructor");
		KettleEnvironment.init();
		
		TransMeta transMeta = new TransMeta("esbdemo.ktr");
		 trans = new Trans(transMeta);
		

		trans.prepareExecution(null);

	
		rp = trans.addRowProducer("Injector", 0);
	
	 rowMeta = new RowMeta();
	rowMeta.addValueMeta(new ValueMeta("text", ValueMetaInterface.TYPE_STRING));
	rowMeta.addValueMeta(new ValueMeta("created_at", ValueMetaInterface.TYPE_DATE));
	rowMeta.addValueMeta(new ValueMeta("hashtag0", ValueMetaInterface.TYPE_STRING));
	rowMeta.addValueMeta(new ValueMeta("hashtag1", ValueMetaInterface.TYPE_STRING));
	rowMeta.addValueMeta(new ValueMeta("hashtag2", ValueMetaInterface.TYPE_STRING));
	rowMeta.addValueMeta(new ValueMeta("hashtag3", ValueMetaInterface.TYPE_STRING));
	rowMeta.addValueMeta(new ValueMeta("hashtag4", ValueMetaInterface.TYPE_STRING));
	rowMeta.addValueMeta(new ValueMeta("retweet_count", ValueMetaInterface.TYPE_STRING));
	rowMeta.addValueMeta(new ValueMeta("delete", ValueMetaInterface.TYPE_STRING));
	rowMeta.addValueMeta(new ValueMeta("id", ValueMetaInterface.TYPE_STRING));
	trans.startThreads();
	}
	
	public void importtwitter(Status test) throws KettleException{
		
		String text = test.getText();
		long id = test.getId();
		String hashtag0 = "#emtpy";
		String hashtag1 = "#emtpy";
		String hashtag2 = "#emtpy";
		String hashtag3 = "#emtpy";
		String hashtag4 = "#emtpy";
		if(test.getHashtagEntities().length >0 && test.getHashtagEntities()[0]!=null){
		 hashtag0 = test.getHashtagEntities()[0].getText();
		}
		if(test.getHashtagEntities().length >1 && test.getHashtagEntities()[1]!=null){
		 hashtag1 = test.getHashtagEntities()[1].getText();
		}
		if(test.getHashtagEntities().length >2 &&test.getHashtagEntities()[2]!=null){
		 hashtag2 = test.getHashtagEntities()[2].getText();
		}
		if(test.getHashtagEntities().length >3 && test.getHashtagEntities()[3]!=null){
		 hashtag3 = test.getHashtagEntities()[3].getText();
		}
		if(test.getHashtagEntities().length >4 && test.getHashtagEntities()[4]!=null){
		 hashtag4 = test.getHashtagEntities()[4].getText();
		}
		
		long retweet_count = test.getRetweetCount();
		Date created_tmp = test.getCreatedAt();
		boolean delete = test.isTruncated();
		Object[] row = new Object[10];
        row[0] = text;
        row[1] = created_tmp;
        row[2] = hashtag0;
        row[3] = hashtag1;
        row[4] = hashtag2;
        row[5] = hashtag3;
        row[6] = hashtag4;
        row[7] = Long.toString(retweet_count);
        row[8] = Boolean.toString(delete);
        row[9] = Long.toString(id);
        System.out.println("process");
        
        
       batchjob(row);

	}
	
	public void batchjob(Object[] row){
		rp.putRow(rowMeta, row);
		i++;
		
		if(i ==100){
			//rp.finished();
			//trans.waitUntilFinished();
			i = 0;
		}
	}
}
