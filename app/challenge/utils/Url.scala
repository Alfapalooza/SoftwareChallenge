package challenge.utils

case class Url private (host: Host, path: Path = Path(Nil)){
  def /(newPath: String): Url = copy(path = path / newPath)
  def ?&(newQuery: (String, String)): Url = copy(path = path ?& newQuery)
  def ?&(newQuery: Set[(String, String)]): Url = copy(path = path ?& newQuery)
  override def toString: String = (host.toString + path.toString).trim.replaceAll(" ", "%20")
}

case class Host private (protocol: Protocol = Protocol.http, host: String, port: Option[Port] = None) {
  override def toString: String = protocol.protocol + host + port.fold("")(p => s":${p.port}")
}

object Protocol {
  val http = new Protocol("http://")
  val https = new Protocol("https://")
}
case class Protocol private (protocol: String) extends AnyVal

case class Port private (port: Int) extends AnyVal

case class Path private (path: Seq[String], queries: Queries = Queries(Set.empty[(String, String)])){
  def /(newPath: String): Path = copy(path = path :+ newPath)
  def ?&(newQuery: (String, String)): Path = copy(queries = queries ?& newQuery)
  def ?&(newQuery: Set[(String, String)]): Path = copy(queries = queries ?& newQuery)
  override def toString: String = path.mkString("/", "/", "") + queries.toString
}

case class Queries private (queries: Set[(String, String)]) extends AnyVal {
  def ?&(newQuery: (String, String)): Queries = this.copy(queries + newQuery)
  def ?&(newQuery: Set[(String, String)]): Queries = this.copy(queries ++ newQuery)
  override def toString: String = {
    val query = queries.foldLeft("?")(
      (acc, param) => acc + {
        if (acc == "?") s"${param._1}=${param._2}"
        else s"&${param._1}=${param._2}"
      }
    )
    if(query != "?") query else ""
  }
}