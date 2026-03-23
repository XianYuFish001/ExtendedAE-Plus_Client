package com.fish.extendedae_plus_client.impl

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.FileConfig
import com.electronwill.nightconfig.core.file.FileConfig.builder
import com.electronwill.nightconfig.toml.TomlFormat.instance
import com.fish.extendedae_plus_client.integration.ManagerIntegration
import com.fish.extendedae_plus_client.integration.impl.bean.ImplJech
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import net.minecraft.network.chat.Component
import net.neoforged.fml.loading.FMLPaths
import java.nio.file.Files
import kotlin.concurrent.Volatile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists

object AliasGetter {
    private val pathConfig = FMLPaths.CONFIGDIR.get().resolve("extendedae_plus/stored_alias.toml")
    private var pathConfigOld = pathConfig.parent.resolve("stored_alias.json")
    private val config: FileConfig = builder(pathConfig, instance()).autosave().autoreload().build()

    init {
        this.config.load()

        if (this.pathConfigOld.exists())
            this.convertConfig()
    }

    fun closeConfig() = this.config.close()

    fun convertConfig() {
        val gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
        val obj = gson.fromJson(Files.readString(this.pathConfigOld), JsonObject::class.java) ?: return

        obj.entrySet().stream()
            .filter { entry -> entry.value?.isJsonPrimitive ?: false && !entry.value?.asString.isNullOrBlank() }
            .forEach { entry -> this.config.set(entry.key, entry.value.asString) }
        this.config.save()
        this.pathConfigOld.deleteIfExists()
    }

    /**
     * 向配置中新增或更新别名映射，并刷新内存映射。
     * 
     * @param typeKey 最终搜索关键字（不含冒号），大小写不敏感
     * @param alias  别名
     * @return 是否写入成功
     */
    @Synchronized
    fun addOrUpdateAlias(typeKey: String?, alias: String?): Boolean {
        if (typeKey.isNullOrBlank() || alias.isNullOrBlank()) return false

        this.config.set<String>(listOf(typeKey.lowercase()), alias)
        return true
    }

    /** @return 删除别名的数量 */
    @Synchronized
    fun removeAliases(alias: String?): Int {
        if (alias.isNullOrBlank()) return 0

        val target = alias.trim()
        if (target.isBlank()) return 0

        val toRemove = this.config.entrySet().stream()
            .filter { entry -> entry.getValue<Any>().toString().equals(target, true) }
            .map(Config.Entry::getKey)
            .toList()
        toRemove.forEach { value -> this.config.remove(value) }
        return toRemove.size
    }

    fun findMapping(key: String?): String? {
        if (key.isNullOrBlank()) return null

        return this.config.get(listOf(key.lowercase()))
    }

    /** 收集到处理配方的关键词（按优先级排序） */
    @Volatile
    private var recipeKeywords: MutableList<KeywordGroup> = ArrayList<KeywordGroup>()

    fun clearRecipeKeywords() {
        recipeKeywords.clear()
    }

    fun getRecipeKeywords(): MutableList<KeywordGroup> {
        recipeKeywords.sortWith(
            Comparator
                .comparing(KeywordGroup::isMapped).reversed()
                .thenComparing(KeywordGroup::priority, Comparator.reverseOrder())
        )
        return recipeKeywords
    }

    fun collectRecipeKeyword(name: String, priority: Int, findMapping: Boolean) {
        val group = KeywordGroup.literal(name)
        group.priority = priority
        if (findMapping) group.findMapping(true)
        recipeKeywords.add(group)
    }

    /**
     * TODO Refactor
     * @param recipe (J)EmiRecipe或RecipeHolder
     */
    @JvmStatic
    fun tryCollectKeywords(recipe: Any?) {
//        recipeKeywords.clear()
//        if (recipe == null) return
//        val keys = HashMap<String, Int>()
//
//        if (ContextModLoaded.emi.isLoaded) {
//            var workstations: MutableList<EmiIngredient> = ArrayList<EmiIngredient>()
//            var categoryName: Component = Component.empty()
//
//            if (recipe is JemiRecipe<*>) {
//                workstations = EmiApi.getRecipeManager().getWorkstations(recipe.recipeCategory)
//                categoryName = recipe.category.title
//
//                keys[recipe.category.title.string] = 3
//                if (recipe.originalId != null) {
//                    keys[recipe.originalId.toString().split("/")[0]] = 2
//                    keys[recipe.originalId.path.split("/")[0]] = 1
//                }
//            } else if (recipe is EmiRecipe) {
//                workstations = EmiApi.getRecipeManager().getWorkstations(recipe.category)
//                categoryName = recipe.category.name
//
//                keys[recipe.category.name.string] = 3
//                if (recipe.id != null) {
//                    keys[recipe.id.toString().split("/")[0]] = 2
//                    keys[recipe.id!!.path.split("/")[0]] = 1
//                }
//            }
//
//            workstations.ifNotEmpty {
//                val workstationKeys = ArrayList<String>()
//                this.reversed().forEach { ingredient ->
//                    ingredient.emiStacks.reversed()
//                        .forEach { stack ->
//                            val name = stack.name
//                            workstationKeys.add(name.string)
//
//                            var key: String? = null
//                            val contents = name.contents
//                            if (contents is PlainTextContents) key = contents.text()
//                            else if (contents is TranslatableContents) key = contents.key
//                            if (key == null) return@forEach
//                            workstationKeys.add(key)
//                        }
//                }
//
//                val groupWorkstation = KeywordGroup(
//                    workstationKeys,
//                    UtilKeyBuilder.of(Patterns.KeywordGroup)
//                        .addStr("workstations")
//                        .args(categoryName.string)
//                        .build()
//                )
//                groupWorkstation.priority = 4
//                groupWorkstation.findMapping(false)
//
//                recipeKeywords.add(groupWorkstation)
//            }
//        }
//
//        if (recipe is RecipeHolder<*>) {
//            keys[recipe.id().identifier().toString().split("/")[0]] = 2
//            keys[recipe.id().identifier().path.split("/")[0]] = 1
//        }
//
//        keys.entries.stream()
//            .filter { entry -> entry.key.isBlank() }
//            .sorted { a, b -> Comparator.reverseOrder<Int>().compare(a.value, b.value) }
//            .forEach { entry ->
//                collectRecipeKeyword(
//                    entry.key,
//                    entry.value,
//                    true
//                )
//            }
    }

    class KeywordGroup(keywords: MutableCollection<String>, groupDescription: Component) {
        private val keywords = ArrayList<String>()
        private var description: Component
        var isMapped: Boolean = false
            private set
        var priority: Int = 0

        init {
            this.keywords.addAll(keywords)
            this.description = groupDescription
        }

        fun matches(nameKey: String, i18nKey: String): Boolean {
            if (this.keywords
                    .map(String::isBlank)
                    .all { it }
            ) return true

            return nameMatches(this.description.string, nameKey)
                    || this.keywords.any { key ->
                nameMatches(key, nameKey)
                        || i18nKeyMatches(key, i18nKey)
            }
        }

        fun getDescription() = if (this.description.string.isEmpty())
            Component.literal(this.keywords[0])
        else this.description

        fun findMapping(mappingKeywords: Boolean) {
            val mappedDesc = findMapping(this.description.string)
            if (!mappedDesc.isNullOrBlank()) {
                this.description = Component.literal(mappedDesc)
                this.isMapped = true
            }

            if (!mappingKeywords) return

            var mapped = false
            val mappedList = this.keywords.map { keyword ->
                val mappedKey = findMapping(keyword)
                if (mappedKey != null) {
                    mapped = true
                    return@map mappedKey
                } else return@map keyword
            }.toList()
            if (mapped) this.isMapped = true

            this.keywords.clear()
            this.keywords.addAll(mappedList)
        }

        val isEmpty get() = keywords.isEmpty()

        override fun equals(other: Any?): Boolean {
            if (other !is KeywordGroup) return false
            return this.keywords == other.keywords
        }

        companion object {
            private var literalGroups: HashMap<String, KeywordGroup>? = null

            fun literal(value: String): KeywordGroup {
                if (literalGroups == null) literalGroups = HashMap()
                return literalGroups!!.computeIfAbsent(value) { _ ->
                    KeywordGroup(
                        mutableListOf(value),
                        Component.empty()
                    )
                }
            }

            private fun nameMatches(matchKey: String?, searchKey: String?): Boolean {
                if (matchKey.isNullOrBlank()) return false
                if (searchKey.isNullOrBlank()) return true

                var matches: Boolean

                val jech = ManagerIntegration<ImplJech>()!!
                matches = jech.contains(matchKey, searchKey)
                matches = matches || jech.contains(searchKey, matchKey)

                matches = matches || matchKey.contains(searchKey, true)
                matches = matches || searchKey.contains(matchKey, true)

                return matches
            }

            private fun i18nKeyMatches(matchKey: String?, searchKey: String?): Boolean {
                if (matchKey.isNullOrBlank() || searchKey.isNullOrEmpty()) return false
                return matchKey.contains(searchKey, true)
                        || searchKey.contains(matchKey, true)
            }
        }
    }
}
