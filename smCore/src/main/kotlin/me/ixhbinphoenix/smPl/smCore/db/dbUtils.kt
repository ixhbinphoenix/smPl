package me.ixhbinphoenix.smPl.smCore.db

import org.postgresql.util.PGobject

class PGEnum<T: Enum<T>>(enumTypeName: String, enumValue: T?): PGobject() {
  init {
    value = enumValue?.name
    type = enumTypeName
  }
}

inline fun <reified T : Enum<T>> getEnumQuery(dbName: String): String {
  val names = enumValues<T>().map { it.name }
  var str = "CREATE TYPE $dbName AS ENUM ("
  for (name in names) {
    str += "'$name'"
    if (names.indexOf(name) != names.size - 1) {
      str += ", "
    }
  }
  str += ");"
  return str
}

inline fun <reified T: Enum<T>> createEnumIfExists(dbName: String): String {
  return "DO\n" +
          "$$\n" +
          "BEGIN\n" +
          "IF NOT EXISTS (SELECT * FROM pg_type WHERE typname = '${dbName.lowercase()}') THEN\n" +
          " ${getEnumQuery<T>(dbName)}\n" +
          "END IF;\n" +
          "END;\n" +
          "$$\n" +
          "LANGUAGE plpgsql;"
}