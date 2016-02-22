package test.edu.upenn.cis455.hw1;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RunAllTests extends TestCase {
        @SuppressWarnings("rawtypes")
        public static Test suite() {
                Class[] testClasses = {
                                MyServerServletsTest.class,
                                BlockingQueueTest.class,
                                DefaultRequestParserTest.class,
                                MyHttpSessionTest.class,
                                MyServletConfigTest.class,
                                MyServletContextTest.class,
                                MyHttpServletRequestTest.class,
                                //MyHttpServletResponseTest.class,

                };
                return new TestSuite(testClasses);
        }
}
