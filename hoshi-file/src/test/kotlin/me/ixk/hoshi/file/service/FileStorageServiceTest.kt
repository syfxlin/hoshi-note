package me.ixk.hoshi.file.service;

import cn.hutool.core.io.FileUtil
import cn.hutool.core.io.IoUtil
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.string.shouldStartWith
import me.ixk.hoshi.file.exception.StorageException
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import java.nio.file.Paths

/**
 * @author Otstar Lin
 * @date 2021/5/23 14:35
 */
@SpringBootTest(properties = ["hoshi.upload-dir=E:/misc/hoshi-test"])
@TestMethodOrder(OrderAnnotation::class)
class FileStorageServiceTest {

    @Autowired
    private lateinit var storageService: StorageService

    private var s1: StorageService.StoreInfo? = null
    private var s2: StorageService.StoreInfo? = null

    @Test
    @Order(1)
    fun init() {
        this.storageService.init();
        FileUtil.exist("E:/misc/hoshi-test") shouldBe true
    }

    @Test
    @Order(2)
    fun store() {
        s1 = this.storageService.store(MockMultipartFile("file", "test file".toByteArray()))
        s1 should {
            if (it != null) {
                it.size shouldBe 9
                it.fileName shouldBe it.path
            }
        }
        s2 = this.storageService.store(MockMultipartFile("file", "test file".toByteArray()), "1")
        s2 should {
            if (it != null) {
                it.size shouldBe 9
                it.path shouldEndWith it.fileName
                it.path shouldStartWith Paths.get("1").toString()
            }
        }
        this.storageService.store(MockMultipartFile("file", "test file".toByteArray()), "1", "2") should {
            if (it != null) {
                it.size shouldBe 9
                it.path shouldStartWith Paths.get("1", "2").toString()
            }
        }
        shouldThrow<StorageException> {
            this.storageService.store(MockMultipartFile("file", "test file".toByteArray()), "../../")
        }
    }

    @Test
    @Order(3)
    fun exist() {
        this.storageService.exist(s1?.fileName) shouldBe true
        this.storageService.exist(s2?.fileName, "1") shouldBe true
    }

    @Test
    @Order(4)
    fun load() {
        this.storageService.load(s1?.fileName).inputStream should {
            IoUtil.readUtf8(it) shouldBe "test file"
        }
        this.storageService.load(s2?.path).inputStream should {
            IoUtil.readUtf8(it) shouldBe "test file"
        }
    }

    @Test
    @Order(5)
    fun loadAll() {
        this.storageService.loadAll().count() shouldBeGreaterThanOrEqual 2
    }

    @Test
    @Order(6)
    fun delete() {
        this.storageService.delete(s1?.fileName)
        this.storageService.exist(s1?.fileName) shouldBe false
    }

    @Test
    @Order(7)
    fun deleteAll() {
        this.storageService.deleteAll()
        this.storageService.exist(s2?.fileName) shouldBe false
    }

    @BeforeAll
    fun clean() {
        FileUtil.del("E:/misc/hoshi-test")
    }
}
