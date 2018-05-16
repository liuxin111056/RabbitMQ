package com.drools.demo;

import java.util.Arrays;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;

public class RuleTest {

	public static final void main(String[] args) throws Exception {  
		RuleTest launcher = new RuleTest();  
		launcher.executeExample();  
	}  

	public int executeExample() throws Exception {  


		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();  

		kbuilder.add( ResourceFactory.newClassPathResource( "discountrule.drl",  
				getClass() ),  
				ResourceType.DRL);  

		if ( kbuilder.hasErrors() ) {  
			System.err.print( kbuilder.getErrors() );  
			return -1;  
		}  

		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();  
		kbase.addKnowledgePackages( kbuilder.getKnowledgePackages() );  


		StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();  


		Order order = new Order();  
		order.setSumprice(159);  
		ksession.execute( Arrays.asList( new Object[]{order} ) );  


		System.out.println( "DISCOUNT IS: " + order.getDiscountPercent() );  

		return order.getDiscountPercent();  

	}  

}
