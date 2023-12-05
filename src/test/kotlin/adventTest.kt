package com.example.advent2022

import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.util.*
import kotlin.math.pow

class AdventTest {


    @Test
    fun advent23_4_2 () {
        val lines = File("src/test/resources/input4").readLines()
        data class CardWins (
            var cardNum: Int = 0,
            var winAmount: Int = 0,
            var cardAmount: Int = 1
        )
        val listOfCardWins = List(lines.size) { lineNum ->
            val cardWins = CardWins()
            cardWins.cardNum = lineNum+1
            cardWins
        }
        lines.mapIndexed { index, line ->
            val lineNum = index + 1
            val winNum = line.split(':')[1].split('|')[0].trim()
                .split(Regex("[ ]+")).map{ it.toInt() }
            val pulledNum = line.split(':')[1].split('|')[1].trim()
                .split(Regex("[ ]+")).map{ it.toInt() }

            listOfCardWins[index].winAmount = winNum.count { pulledNum.contains( it )}
            val repeatNum = listOfCardWins[index].cardAmount

            for (i in lineNum ..minOf( lineNum + listOfCardWins[index].winAmount-1, 217)) {
                repeat(repeatNum) {listOfCardWins[i].cardAmount++}
            }
        }
        var sum = 0L
        listOfCardWins.forEach {
            sum += it.cardAmount
        }
        println(sum)
    }





    @Test
    fun advent23_4_1 () {
        val lines = File("src/test/resources/input4").readLines()

        val sum = lines.map { line ->
            val winNum = line.split(':')[1].split('|')[0].trim()
                .split(Regex("[ ]+")).map{ it.toInt() }
            val pulledNum = line.split(':')[1].split('|')[1].trim()
                .split(Regex("[ ]+")).map{ it.toInt() }

            return@map 2.0.pow(winNum.count { pulledNum.contains( it ) } - 1).toInt()
        }.sum()
        println(sum)
    }


    @Test
    fun advent23_3_2 () {
        val zeilen = File("src/test/resources/input3").readLines()

        // leere Map anlegen mit Key aus einem Paar aus Zeile, Spalte und Value mit Liste der gefundenen Nummern
        val gearMap = mutableMapOf<Pair<Int,Int>,MutableList<Int>>()

        zeilen.forEachIndexed { lineNr, zeile ->
            // Über Regex die Nummer im String raussuchen
            val sumPerLine = Regex("[0-9]+").findAll(zeile).forEach {
                // it ist das MatchResult. Auf die eigentliche Nummer (als String) wird mit .value zugegriffen.
                if (lineNr > 0) {
                    for (i in maxOf(it.range.start-1, 0) .. minOf(it.range.last+1, zeile.length-1)){
                        if (zeilen[lineNr-1][i] == '*') {
                            if (gearMap[lineNr-1 to i] == null) gearMap[lineNr-1 to i] = mutableListOf(it.value.toInt())
                            else gearMap[lineNr-1 to i]!!.add(it.value.toInt())
                        }
                    }
                }
                if (it.range.start != 0 && zeile[it.range.start-1] == '*') {
                    if (gearMap[lineNr to it.range.start-1] == null) gearMap[lineNr to it.range.start-1] = mutableListOf(it.value.toInt())
                    else gearMap[lineNr to it.range.start-1]!!.add(it.value.toInt())
                }
                if (it.range.last != zeile.length-1 && zeile[it.range.last+1] == '*' ) {
                    if (gearMap[lineNr to it.range.last+1] == null) gearMap[lineNr to it.range.last+1] = mutableListOf(it.value.toInt())
                    else gearMap[lineNr to it.range.last+1]!!.add(it.value.toInt())
                }
                if (lineNr < zeilen.size-1) {
                    for (i in maxOf(it.range.start-1, 0) .. minOf(it.range.last+1, zeile.length-1)){
                        if (zeilen[lineNr+1][i] == '*') {
                            if (gearMap[lineNr+1 to i] == null) gearMap[lineNr+1 to i] = mutableListOf(it.value.toInt())
                            else gearMap[lineNr+1 to i]!!.add(it.value.toInt())
                        }
                    }
                }

            }
        }
        println(gearMap)
        val sum = gearMap.filter { (gearCoord, listOfNums) ->
            listOfNums.size == 2
        }.map { (gearCoord, listOfNums) ->
            listOfNums[0] * listOfNums[1]
        }.sum()
        println(sum)
    }




    @Test
    fun advent23_3_1 () {
        val zeilen = File("src/test/resources/input3").readLines()

        val sum = zeilen.mapIndexed { lineNr, zeile ->
            // Über Regex die Nummer im String raussuchen
            val sumPerLine = Regex("[0-9]+").findAll(zeile).filter {
                if (lineNr > 0) {
                    for (i in maxOf(it.range.start-1, 0) .. minOf(it.range.last+1, zeile.length-1)){
                        if (zeilen[lineNr-1][i] != '.') return@filter true
                    }
                }
                if (it.range.start != 0 && zeile[it.range.start-1] != '.') return@filter true
                if (it.range.last != zeile.length-1 && zeile[it.range.last+1] != '.' ) return@filter true
                if (lineNr < zeilen.size-1) {
                    for (i in maxOf(it.range.start-1, 0) .. minOf(it.range.last+1, zeile.length-1)){
                        if (zeilen[lineNr+1][i] != '.') return@filter true
                    }
                }
                false
            }.map { it.value.toInt() }.sum()
            sumPerLine

        }.sum()
        println(sum)
    }





    @Test
    fun advent23_2 () {
        data class Spielrunde (
            var green: Int = 0,
            var red: Int = 0,
            var blue: Int = 0,
        )

        val zeilen = File("src/test/resources/input2").readLines()
        val maxRed = 12
        val maxGreen = 13
        val maxBlue = 14
        var sum = 0
        val gesamt = zeilen.map { zeile ->
            val gameNumber = zeile.split(':')[0].replace("Game ", "").toInt()
            val ergListe = zeile.split(':')[1].split(';').map { runde ->
                val rundeErg: Spielrunde = Spielrunde()
                runde.split(',').map { farbe ->
                    val farbname = farbe.trim().split(' ')[1]
                    val farbErg = farbe.trim().split(' ')[0].toInt()

                    when (farbname) {
                        "red" -> rundeErg.red = farbErg
                        "green" -> rundeErg.green = farbErg
                        "blue" -> rundeErg.blue = farbErg
                    }
                }
                rundeErg
                // println(" rot: ${rundeErg.red} grün: ${rundeErg.green} blau: ${rundeErg.blue}")
            }
            gameNumber to ergListe
        }
        // println(gesamt)
        // Summe der Spiele, die möglich sind
        val teilliste = gesamt.filter { (gameNumber, rounds) ->
                rounds.all {
                    it.red <= maxRed && it.blue <=maxBlue && it.green <= maxGreen
                }
             }
        println (teilliste.sumOf { it.first }) // = map { it.first }.sum()
        // Summe der Power von dem was nötig wäre
        val bla = gesamt.map { (gameNumber, rounds) ->
            val rMax = rounds.maxOf { it.red }
            val bMax = rounds.maxOf { it.blue }
            val gMax = rounds.maxOf { it.green }
            rMax * bMax * gMax
        }
        println(bla.sum())

    }








    @Test
    fun advent23_1_1 () {
        val lines = File("src/test/resources/input").readLines()
        var sum = 0
        lines.map { zeile ->
            var num1 = 0
            var num2 = 0

            for (x in zeile.indices) {
                if (zeile[x].isDigit()) {
                    num1 = zeile[x].toString().toInt()
                    break
                }
            }
            var zeileRev = zeile.reversed()
            for (x in zeileRev.indices) {
                 if (zeileRev[x].isDigit()) {
                     num2 = zeileRev[x].toString().toInt()
                     break
                 }
            }
            sum += ("$num1" + "$num2").toInt()
        }
        println (sum)


        //Felix Lösung:
        val sum2 = lines.map { line ->
            "${line.first {it.isDigit()}}${line.last {it.isDigit()}}".toInt()
            }.sum()
        println (sum2)
    }

    @Test
    fun advent23_1_2 () {
        val lines = File("src/test/resources/input").readLines()
        var sum = 0
        lines.map { zeile ->
            var num1 = 0
            var num2 = 0

            println(zeile.findAnyOf(listOf("one","two","three","four","five","six","seven","eight","nine")).toString())
// siehe Chat mit Felix vom 1.12.2023 =D
        }
    }


    @Test
    fun horst() {
        print("horst")
    }

}



