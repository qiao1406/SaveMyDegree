import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import scala.io.Source
import scala.collection.mutable.ArrayBuffer


object ReadFile {
    
        def main ( args: Array[String] ){
                
                //  Arrays to store vertexes and edges
                val vertexArr = new ArrayBuffer[(String)]()
                val edgeArr = new ArrayBuffer[Edge[String]]()
                
                // read network description file from disk
                 val inputFile = Source.fromFile(raw"/home/qx/SaveMyDegree/network.txt")
                 val lines = inputFile.getLines()
     
                 while (  lines.hasNext  ) {  // deal with one single line
                         
                         val s = lines.next().split(" ")
                         
                         // 避免重复添加顶点
                         if (  vertexArr.exists( { x:String ⇒ x == s(0) } ) == false  )  {
                                 vertexArr += s(0)
                         }
                         if (  vertexArr.exists( { x:String ⇒ x == s(1) } ) == false  )  {
                                 vertexArr += s(1)
                         }
                         
                         // 添加边
                         edgeArr += Edge(  s(0),  s(1),  s(2) )
                         
                 }
                         
                 
                 inputFile.close()
         }
    
    
}