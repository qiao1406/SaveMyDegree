import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.graphx.Edge
import org.apache.spark.graphx.Graph
import org.apache.spark.graphx.EdgeContext


object SimpleGraphX4 {
        
        def sendMsg ( ec: EdgeContext[Int, String, Int]): Unit = ec.sendToDst(ec.srcAttr+1)
        
        def mergeMsg ( a: Int, b: Int): Int = math.max(a, b) 
        
        def propagateEdgeCount ( g: Graph[Int, String] ): Graph[Int, String] = {
                val verts = g.aggregateMessages[Int](sendMsg, mergeMsg)
                val g2 = Graph(verts, g.edges)
                
                val check = g2.vertices.join(g.vertices).
                        map(x ⇒ x._2._1 - x._2._2 ).reduce(_ + _)
                
                if ( check > 0 ) propagateEdgeCount(g2) else g
        }
        
        def main ( args: Array[String] ) {
                
                Logger.getLogger("org").setLevel(Level.ERROR)
                
                val conf = new SparkConf().setAppName("Test GraphX 4")
                val sc = new SparkContext("local", "test", conf)
                
                val myVertices = sc.makeRDD(Array((1L, "Ann"), (2L, "Bill"), (3L, "Charles"), 
                                (4L, "Diane"), (5L, "Went to gym this morning" )))
                 val myEdges = sc.makeRDD(Array(Edge(1L, 2L, "is-friend-with"), Edge(2L, 3L, "is-friend-with"),
                               Edge(3L, 4L, "is-friend-with" ), Edge(4L, 5L, "Likes-staus"), Edge(3L, 5L, "Wrote-staus")))
                 val myGraph = Graph(myVertices, myEdges) 
                 
                 val initialGraph = myGraph.mapVertices( (_, _) ⇒ 0 )
                 propagateEdgeCount(initialGraph).vertices.collect().foreach(v ⇒println(v))
                 
                              
                
        }
}