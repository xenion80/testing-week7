package com.codingShuttle.TestingApp.demo;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Slf4j
class DemoApplicationTests {

	@BeforeEach
	void setUp(){
		log.info("Starting the method, setting up config");
	}

	@BeforeAll
    static void setUpOnce(){
		log.info("Setup once....");
	}

	@AfterEach
	void tearDown(){
		log.info("Tearing down the method");
	}
	@AfterAll
	static void tearDownOnce(){
		log.info("Tearing down all....");
	}

	@Test
	void testNumberOne() {

		int a=5;
		int b=3;
		int result=addTwoNumbers(a,b);
//		Assertions.assertEquals(8,result);
		assertThat(result).isEqualTo(8).isCloseTo(9, Offset.offset(1));
		assertThat("Apple")
				.isEqualTo("Apple")
				.startsWith("App")
				.endsWith("le")
				.hasSize(5);
	}

	@Test
	void testNumberTwoNumbers_whenDenominatorIsZero_ThenArithmeticException(){
		int a=5;
		int b=0;
		assertThatThrownBy(()->divideTwoNumbers(a,b))
				.isInstanceOf(ArithmeticException.class)
				.hasMessage("Tried to divide by zero");
//		double result=divideTwoNumbers(a,b);
	}
	int addTwoNumbers(int a, int b){
		return a+b;
	}
	double divideTwoNumbers(int a, int b) {


		try {
			return a/b;
		}catch (ArithmeticException e){
			log.error("Arithmetic exception occured:"+e.getLocalizedMessage());
			throw new ArithmeticException(e.getLocalizedMessage());
		}
	}


}