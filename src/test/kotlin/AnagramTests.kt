import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

// static, only run once as we load the class
val words = File("./scrabble.txt")
    .readLines()
    .plus(
        listOf(
            "A",
            "O",
            "I",
        )
    )
    .sorted()

//@ExtendWith(ApprovalsExtension::class)
class AnagramTests {
    @Test
    fun `load words`() {
        assertEquals(
            178694,
            words.size
        )
    }

    // multi-word anagram
    // filter potential words that can be made from the subject
    // filter multi-word anagram that mirror the subject
    
    @Test
    fun `can be made from the letters in`() {

        assertTrue(
            "A"
                .couldBeMadeFromLettersIn(
                    "A CAT"
                )
        )


        assertTrue(
            "AA"
                .couldBeMadeFromLettersIn(
                    "A CAT"
                )
        )
        assertTrue(
            "ACT"
                .couldBeMadeFromLettersIn(
                    "A CAT"
                )
        )
        assertTrue(
            "CAT"
                .couldBeMadeFromLettersIn(
                    "A CAT"
                )
        )


        assertFalse(
            "H"
                .couldBeMadeFromLettersIn(
                    "A CAT"
                )
        )
        assertFalse(
            "AAH"
                .couldBeMadeFromLettersIn(
                    "A CAT"
                )
        )
        assertFalse(
            "TAT"
                .couldBeMadeFromLettersIn(
                    "A CAT"
                )
        )

        // boundary conditions
        assertTrue(
            ""
                .couldBeMadeFromLettersIn(
                    "A CAT"
                )
        )
        assertTrue(
            ""
                .couldBeMadeFromLettersIn(
                    ""
                )
        )
    }

    @Test
    fun `anagrams For A CAT`() {
        val input = "A CAT"
        assertEquals(
            listOf(
                "A ACT",
                "A CAT",
                "ACTA",
            ),
            words.anagramsFor(input, Int.MAX_VALUE),
        )
    }
    @Test
    fun `anagrams For ANAGRAM`() {
        val input = "ANAGRAM"
        assertEquals(
            listOf(
                "A ACT",
                "A CAT",
                "ACTA",
            ),
            words.anagramsFor(input, Int.MAX_VALUE),
        )
    }

    @Test
    fun `anagrams For REFACTORING`() {
        val input = "REFACTORING"
        assertEquals(
            listOf(
                "A ACT",
                "A CAT",
                "ACTA",
            ),
            words.anagramsFor(input, Int.MAX_VALUE),
        )
    }
    @Test
    fun `anagrams For REFACTORING TO KOTLIN depth 3`() {
        val input = "REFACTORING TO KOTLIN"
        assertEquals(
            listOf(
                "A ACT",
                "A CAT",
                "ACTA",
            ),
            words.anagramsFor(
                input,
                depth = 3,
                ),
        )
    }
//    @Test
//    fun `anagrams for ANAGRAM`(approver: Approver) {
//        approver.assertApproved(
//            words.anagramsFor("ANAGRAM").joinToString("\n")
//        )
//    }

//    companion object {
//        @RegisterExtension
//        @JvmField
//        val approvals = ApprovalsExtension("src/test/kotlin")
//    }
}

fun List<String>.anagramsFor(input: String, depth: Int = Int.MAX_VALUE): List<String> {
    val result: MutableList<String> = mutableListOf<String>()
    process(
        input.replace(" ", ""),
        this,
        { result.add(it) },
        depth = depth,
    )
    return result
}

private fun process(
    input: String,
    words: List<String>,
    collector: (String) -> Unit,
    prefix: String = "",
    depth: Int
) {
    // get all candidates
    val candidateWords = words.filter {
        it.couldBeMadeFromLettersIn(input)
    }
    val remainingCandidates = candidateWords.toMutableList()
//        println("_words ${_words.toString()}")
    candidateWords
        .forEach { word ->
            val remainingLetters = input.minusLettersIn(word)
            if (remainingLetters.isNotBlank()) {
//                println("word: $word,, remaining letters: [$remainingLetters]")
//                println("$prefix $word [$remainingLetters]")
                if(depth > 1) {
                    process(
                        remainingLetters,
                        remainingCandidates,
                        collector,
                        prefix = "$prefix $word",
                        depth - 1,
                    )
                }
            } else {
                collector("$prefix $word".trim())
//                println("$prefix $word")
//                println("word--no-remaining-letters: $word")

                /* else {
                                               println("word--no-remaining-letters: $word")
                                           }*/
            }
            remainingCandidates.remove(word)
        }
}

private fun String.minusLettersIn(word: String): String {
    val lettersList = this.toMutableList()
    word.forEach { char ->
        if (!lettersList.remove(char)) error("BAD")
    }
    return String(lettersList.toCharArray())
}

private fun String.couldBeMadeFromLettersIn(letters: String): Boolean {
    if(this.length > letters.length)
        return false
    val lettersList = letters.toMutableList()
// test presence of letters by looping
//    removing chars guarantee testing letter repetition
    this.forEach { char ->
        if(!lettersList.remove(char)) return false
    }
    return true

}
