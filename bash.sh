# ── Target: all screen/component .kt files (excludes theme/ files) ──────────
FILES=$(find feature/presentation/src/commonMain/kotlin/com/smach/zapmancer/features \
  -name "*.kt" \
  ! -path "*/theme/*" \
  ! -path "*/common/components/*")

# ── Pill shapes (999dp, 100dp, 50dp → extraLarge) ───────────────────────────
echo "$FILES" | xargs sed -i \
  -e 's/RoundedCornerShape(999\.dp)/MaterialTheme.shapes.extraLarge/g' \
  -e 's/RoundedCornerShape(100\.dp)/MaterialTheme.shapes.extraLarge/g' \
  -e 's/RoundedCornerShape(50)/MaterialTheme.shapes.extraLarge/g'

# ── Large / card shapes (16dp, 20dp → large) ────────────────────────────────
echo "$FILES" | xargs sed -i \
  -e 's/RoundedCornerShape(16\.dp)/MaterialTheme.shapes.large/g' \
  -e 's/RoundedCornerShape(20\.dp)/MaterialTheme.shapes.large/g'

# ── Medium shapes (12dp, 28dp → medium) ─────────────────────────────────────
echo "$FILES" | xargs sed -i \
  -e 's/RoundedCornerShape(12\.dp)/MaterialTheme.shapes.medium/g' \
  -e 's/RoundedCornerShape(28\.dp)/MaterialTheme.shapes.medium/g'

# ── Small shapes (8dp → small) ───────────────────────────────────────────────
echo "$FILES" | xargs sed -i \
  -e 's/RoundedCornerShape(8\.dp)/MaterialTheme.shapes.small/g'

# ── Extra-small shapes (4dp, 3dp → extraSmall) ───────────────────────────────
echo "$FILES" | xargs sed -i \
  -e 's/RoundedCornerShape(4\.dp)/MaterialTheme.shapes.extraSmall/g' \
  -e 's/RoundedCornerShape(3\.dp)/MaterialTheme.shapes.extraSmall/g'

# ── Remove all .shadow() calls (Nike: zero elevation) ────────────────────────
echo "$FILES" | xargs sed -i \
  -e 's/\.shadow([^)]*)//' \

echo "✅ Done. Run: ./gradlew :feature:presentation:compileCommonMainKotlinMetadata"

