import org.apache.spark.graphx._
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.log4j.Logger
import org.apache.log4j.Level
import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import org.apache.hadoop.conf.Configuration
import java.io.FileSystem
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.FileUtil
import org.apache.hadoop.fs.Path

object  SimpleGraphX {
        
  
        
        def main (  args: Array[String] ) {
                
                Logger.getLogger("org").setLevel(Level.ERROR)
                
                val conf = new SparkConf().setAppName("Simple App")
                val sc = new SparkContext("local",  "test", conf)
                
                // 读顶点数据
                val v = new ArrayBuffer[(Long, String)]
                val fv = Source.fromFile("/home/qx/SaveMyDegree/vertex.txt")
                
                for ( line ← fv.getLines() ) {
                        val s = line.replace("\n", "").split(" ")
//                        val tempEdge = (s(0).toLong, s(1).toLong, s(2).toString())
                       // val tempEdge = (1L, 2L, "xx")
                        v += Tuple2(s(0).toLong, s(1).toString())
                }
                
                val myVertexes = sc.makeRDD(v)
                fv.close()
                
                // 读边数据
               // val e = new ArrayBuffer[Edge[(Long, Long, Int)]]
                val e = new ArrayBuffer[Edge[Int]]
                val fe = Source.fromFile("/home/qx/SaveMyDegree/edge.txt")
                
                for ( line ← fe.getLines() ) {
                        val s = line.replace("\n", "").split(" ")
//                        e += Tuple3(s(0).toLong, s(1).toLong, s(2).toInt )
                        e += Edge(s(0).toLong, s(1).toLong, s(2).toInt)
                }
                
                val myEdges = sc.makeRDD(e)
                fe.close()
                
                val myGraph = Graph(myVertexes, myEdges)
                
                val conf1 = new Configuration
                conf1.set("fs.defaultFS",  "hdfs://localhost")
                val fs = FileSystem.get(conf1)
                FileUtil.copyMerge(fs, new Path("/usr/cloudera/myGraphVertices/"), 
                                fs, new Path("/usr/cloudera/myGraphVerticesFile"), false, conf1, null)
                
                
//                myGraph.vertices.saveAsObjectFile("myGraphVertices")
//                myGraph.edges.saveAsObjectFile("myGraphEdges")
//                val myGraph2 = Graph(
//                                sc.objectFile[Tuple2[VertexId, String]]("myGraphVertices"),
//                                sc.objectFile[Edge[Int]]("myGraphEdges"))
                
               // myGraph.vertices.collect.foreach(println)
              //for(e ← myGraph.edges.collect() ) println(e)
             //myGraph.edges.collect().foreach(a ⇒ println(a))
             //myGraph.triplets.collect().foreach(a ⇒ println(a))
              //myGraph.mapTriplets(t ⇒ t.attr > 200 && t.attr <= 300 ).triplets.collect().foreach(x ⇒ if (x.attr== true) println(x))
                //myGraph.aggregateMessages[Int](_.sendToSrc(1), _ + _).join(myGraph.vertices).map(_._2.swap).collect().foreach(a ⇒  println(a))
                
//                val graph = GraphLoader.edgeListFile(sc, "/home/qx/SaveMyDegree/cit-HepTh.txt")
                
                
                
                //val v = graph.pageRank(0.001).vertices
                //val v1 = v.reduce( (a, b) ⇒ if ( a._2 > b._2 ) a else b )
                //print(v1)`
                //graph.vertices.take(10).foreach(println(_))
                //val maxIntDegree = graph.inDegrees.reduce(  ( a, b) => if ( a._2 >  b._2 )  a else b ) 
                //print(maxIntDegree)
                //graph.vertices.collect().foreach(println(_))
                
        }
        
}