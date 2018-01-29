import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import org.apache.log4j.Logger
import org.apache.log4j.Level


object GenerateGraph {
        /*
         * 建立并且返回图
         * */
        
        def getGraph ( v: RDD[(Long, IPVerticeNode)], e: RDD[Edge[EdgeLink]] ): 
                Graph[IPVerticeNode, EdgeLink] = { 
                println("<图建立>完成")
                return Graph(v, e)
        }
        
        def getEdgesRDD (sc: SparkContext, fileName: String ): RDD[Edge[EdgeLink]] = {
                 /*
                 *  输入文件名根据文件内容建立边的RDD
                 * */
                val e = new ArrayBuffer[Edge[EdgeLink]]
                val fe = Source.fromFile(fileName)
                
                for ( line ← fe.getLines() ) {
                        val s = line.replace("\n", "").split(" ")
                        e += Edge(s(0).toLong, s(1).toLong, new EdgeLink(s(2).toInt))
                }
                
                fe.close()
                println("***边读入完毕***")
                println()
                return sc.makeRDD(e)
                
        }
        
        def getVerticesRDD (sc: SparkContext, fileName: String ): RDD[(Long, IPVerticeNode)] = {
                /*
                 *  输入文件名根据文件内容建立顶点的RDD
                 * */
                
                val v = new ArrayBuffer[(Long, IPVerticeNode)]  // 暂定
                val fv = Source.fromFile(fileName)
                
                for ( line ← fv.getLines() ) {
                        val s = line.replace("\n", "").split(" ")
                        v += Tuple2(s(0).toLong, new IPVerticeNode(s(1).toString()))
                }
                
                fv.close()
                println("***顶点读入完毕***")
                println()
                return sc.makeRDD(v)
                
        }
    
        def main ( args: Array[String] ){
                
                Logger.getLogger("org").setLevel(Level.ERROR)
                
                val conf = new SparkConf().setAppName("Generate Graph Structure")
                val sc = new SparkContext("local",  "test", conf)
                val fv = "/home/qx/EclipseWorkspace/vertices.txt"
                val fe = "/home/qx/EclipseWorkspace/edges.txt"
                val g = getGraph(getVerticesRDD(sc, fv), getEdgesRDD(sc, fe))
              
         }
    
    
}