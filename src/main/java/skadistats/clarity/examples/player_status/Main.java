package skadistats.clarity.examples.player_status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skadistats.clarity.event.Insert;
import skadistats.clarity.io.Util;
import skadistats.clarity.model.DTClass;
import skadistats.clarity.model.Entity;
import skadistats.clarity.model.FieldPath;
import skadistats.clarity.processor.entities.Entities;
import skadistats.clarity.processor.runner.Context;
import skadistats.clarity.processor.entities.OnEntityUpdated;
import skadistats.clarity.processor.entities.UsesEntities;
import skadistats.clarity.processor.reader.OnTickEnd;
import skadistats.clarity.processor.runner.SimpleRunner;
import skadistats.clarity.processor.sendtables.DTClasses;
import skadistats.clarity.processor.sendtables.OnDTClassesComplete;
import skadistats.clarity.source.MappedFileSource;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@UsesEntities
public class Main {

    private final Logger log = LoggerFactory.getLogger(Main.class.getPackage().getClass());

    @Insert
    private DTClasses dtClasses;

    @Insert
    private Entities entities;

    @Insert
    private Context ctx;

    private DTClass playerResourceClass;
    private final PlayerResourceLookup[] playerLookup = new PlayerResourceLookup[10];
    private final Entity[] heroLookup = new Entity[10];
    private final List<Runnable> deferredActions = new ArrayList<>();
    private final String[] entityAttributes = {"hero","m_hOwnerEntity","m_iCurrentLevel","m_nTotalDamageTaken","m_iAttackCapabilities","m_iSpecialAbility","m_iTaggedAsVisibleByTeam","m_anglediff","m_NetworkActivity","m_NetworkSequenceIndex","m_iCurrentXP","m_hAbilities.0000","m_hAbilities.0001","m_hAbilities.0002","m_hAbilities.0003","m_hAbilities.0004","m_hAbilities.0005","m_hAbilities.0006","m_hAbilities.0007","m_hAbilities.0008","m_hAbilities.0009","m_hAbilities.0010","m_hAbilities.0011","m_hAbilities.0012","m_hAbilities.0013","m_hAbilities.0014","m_hAbilities.0015","m_hAbilities.0016","m_hAbilities.0017","m_hAbilities.0018","m_hAbilities.0019","m_hAbilities.0020","m_hAbilities.0021","m_hAbilities.0022","m_hAbilities.0023","m_hAbilities.0024","m_hAbilities.0025","m_hAbilities.0026","m_hAbilities.0027","m_hAbilities.0028","m_hAbilities.0029","m_hAbilities.0030","m_hAbilities.0031","m_hAbilities.0032","m_hAbilities.0033","m_hAbilities.0034","m_iHealth","m_lifeState","m_fFlags","CBodyComponent.m_cellX","CBodyComponent.m_cellY","CBodyComponent.m_cellZ","CBodyComponent.m_vecX","CBodyComponent.m_vecY","CBodyComponent.m_vecZ","CBodyComponent.m_hParent","CBodyComponent.m_flScale","CBodyComponent.m_flPlaybackRate","CBodyComponent.m_nNewSequenceParity","CBodyComponent.m_nResetEventsParity","CBodyComponent.m_name","CBodyComponent.m_hierarchyAttachName","CBodyComponent.m_hModel","CBodyComponent.m_bClientClothCreationSuppressed","CBodyComponent.m_MeshGroupMask","CBodyComponent.m_nIdealMotionType","CBodyComponent.m_bIsAnimationEnabled","CBodyComponent.m_bUseParentRenderBounds","CBodyComponent.m_materialGroup","CBodyComponent.m_flWeight","CBodyComponent.m_nAnimLoopMode","CBodyComponent.m_nOutsideWorld","m_pEntity.m_nameStringableIndex","m_bVisibleinPVS","m_iMaxHealth","m_takedamage","m_bTakesDamage","m_nTakeDamageFlags","m_nPlatformType","m_MoveCollide","m_MoveType","m_flCreateTime","m_ubInterpolationFrame","m_iTeamNum","m_hEffectEntity","m_fEffects","m_flElasticity","m_bAnimatedEveryTick","m_flNavIgnoreUntilTime","m_nBloodType","m_nRenderMode","m_nRenderFX","m_clrRender","m_bRenderToCubemaps","m_bNoInterpolate","m_nInteractsAs","m_nInteractsWith","m_nInteractsExclude","m_nEntityId","m_nOwnerId","m_nHierarchyId","m_nCollisionGroup","m_nCollisionFunctionMask","m_usSolidFlags","m_nSolidType","m_triggerBloat","m_nSurroundType","m_CollisionGroup","m_nEnablePhysics","m_flCapsuleRadius","m_iGlowType","m_iGlowTeam","m_nGlowRange","m_nGlowRangeMin","m_glowColorOverride","m_bFlashing","m_flGlowTime","m_flGlowStartTime","m_flGlowBackfaceMult","m_fadeMinDist","m_fadeMaxDist","m_flFadeScale","m_flShadowStrength","m_nObjectCulling","m_nAddDecal","m_flDecalHealBloodRate","m_flDecalHealHeightRate","m_bShouldAnimateDuringGameplayPause","m_bInitiallyPopulateInterpHistory", "m_nLod","m_bSelectionRingVisible","m_flMana","m_flMaxMana","m_flManaThinkRegen","m_flHealthThinkRegen","m_nHealthBarOffsetOverride","m_bIsPhantom","m_bIsAncient","m_bIsBossCreature","m_bIsNeutralUnitType","m_bIsSummoned","m_bCanBeDominated","m_bCanRespawn","m_bIsClone","m_bHasUpgradeableAbilities","m_iBKBChargesUsed","m_iAeonChargesUsed","m_flRefresherUseTime","m_flRefresherLastCooldown","m_flLastDealtDamageTime","m_iBotDebugData","m_bIsMoving","m_bCanUseWards","m_bCanUseAllItems","m_hNeutralSpawner","m_flManaRegen","m_flHealthRegen","m_iAttackRange","m_iMoveSpeed","m_flTauntCooldown","m_flTauntCooldown2","m_fRevealRadius","m_flBaseAttackTime","m_iDayTimeVisionRange","m_iNightTimeVisionRange","m_flPhysicalArmorValue", "m_flMagicalResistanceValue", "m_iXPBounty","m_iXPBountyExtra","m_iGoldBountyMin","m_iGoldBountyMax","m_nPlayerOwnerID","m_flLastDamageTime","m_flLastAttackTime","m_nUnitState64","m_nUnitModelVariant","m_iIsControllableByPlayer64","m_iUnitNameIndex","m_iDamageMin","m_iDamageMax","m_iDamageBonus","m_bIsWaitingToSpawn","m_nUnitLabelIndex","m_hOwnerNPC","m_hItems.0000","m_hItems.0001","m_hItems.0002","m_hItems.0003","m_hItems.0004","m_hItems.0005","m_hItems.0006","m_hItems.0007","m_hItems.0008","m_hItems.0009","m_hItems.0010","m_hItems.0011","m_hItems.0012","m_hItems.0013","m_hItems.0014","m_hItems.0015","m_hItems.0016","m_hItems.0017","m_hItems.0018","m_iParity","m_bStashEnabled","m_hTransientCastItem","m_iCurShop","m_bStolenScepter","m_bShouldDoFlyHeightVisual","m_flStartSequenceCycle","m_flStrength","m_flAgility","m_flIntellect","m_flStrengthTotal","m_flAgilityTotal","m_flIntellectTotal","m_iRecentDamage","m_iPrimaryAttribute","m_flDeathTime","m_iAbilityPoints","m_iTotalAbilityPoints","m_flRespawnTime","m_flRespawnTimePenalty","m_bScriptDisableRespawns","m_iPlayerID","m_iHeroFacetID","m_hReplicatingOtherHeroModel","m_bReincarnating","m_bCustomKillEffect","m_flSpawnedAt","m_bvDisabledHitGroups.0000","m_hGoalEntity"};
    private final String[] heroAttr = {"CDOTA_Unit_Hero_"};

    @OnDTClassesComplete
    protected void onDtClassesComplete() {
        playerResourceClass = dtClasses.forDtName("CDOTA_PlayerResource");
        for (int i = 0; i < 10; i++) {
            playerLookup[i] = new PlayerResourceLookup(playerResourceClass, i);
        }
    }

    @OnEntityUpdated
    protected void onEntityUpdated(Entity e, FieldPath[] changedFieldPaths, int nChangedFieldPaths) {
        if (e.getDtClass() == playerResourceClass) {
            for (int p = 0; p < 10; p++) {
                PlayerResourceLookup lookup = playerLookup[p];
                if (lookup.isSelectedHeroChanged(e, changedFieldPaths, nChangedFieldPaths)) {
                    int playerIndex = p;
                    deferredActions.add(() -> {
                        int heroHandle = lookup.getSelectedHeroHandle(e);
                        Entity heroEntity = entities.getByHandle(heroHandle);
                        heroLookup[playerIndex] = heroEntity;
                    });
                }
            }
        }
    }
    private static boolean hasMatchingSubstring(String str, String[] substrings) {
        for (String substring : substrings){
            if (str.contains(substring)) {
                return true;
            }
        }
        return false;
    }

    @OnTickEnd
    protected void onTickEnd(boolean synthetic) {
        if (ctx.getTick()%30 != 0) return;
        for (int p = 0; p < 10; p++) {
            Entity lookup = heroLookup[p];
            if (lookup == null){
                deferredActions.forEach(Runnable::run);
                deferredActions.clear();
                return;
            }
        }
        String line = String.format("%06d,", ctx.getTick());
        for (int p = 0; p < 10; p++) {
            Entity lookup = heroLookup[p];
            String table = String.format("%s", lookup);
            List<String> properties = new ArrayList<>();
            for (String row : table.split("\n")) {
                if (row.contains("|")){
//                    System.out.format(row + "\n");
                    String[] cells = row.split("\\|"); // Need to escape "|" as it's a special regex character
                    String[] heroCells = row.split(":");
                    if (cells.length > 3 && hasMatchingSubstring(cells[2].trim(), entityAttributes)) {
                        properties.add(cells[3].trim());
                    } else if (heroCells.length > 3 && hasMatchingSubstring(heroCells[3], heroAttr)) {
                        properties.add(heroCells[3].replace("|", "").trim());
                    }
                }
            }
            String separator = ", "; // Choose your desired separator
            line = line + String.join(separator, properties)+",";
        }
        System.out.format(line + "\n");
    }

    public void run(String[] args) throws Exception {
        long tStart = System.currentTimeMillis();
        MappedFileSource s = new MappedFileSource(args[0]);
        SimpleRunner runner = new SimpleRunner(s);
        String header = "tick,";
        for (int p = 0; p < 10; p++) {
            for (int i = 0; i < entityAttributes.length; i++) {
                header = header + String.format("player_%d_", p) + entityAttributes[i] + ",";
            }
        }
        System.out.format(header + "\n");
        runner.runWith(this);
        long tMatch = System.currentTimeMillis() - tStart;
        log.info("total time taken: {}s", (tMatch) / 1000.0);
        s.close();
    }

    public static void main(String[] args) throws Exception {
        try {
            new Main().run(args);
        } catch (Exception e) {
            Thread.sleep(1000);
            throw e;
        }
    }

    private static class PlayerResourceLookup {

        private final FieldPath fpSelectedHero;

        private PlayerResourceLookup(DTClass playerResourceClass, int idx) {
            this.fpSelectedHero = playerResourceClass.getFieldPathForName(
                    format("m_vecPlayerTeamData.%s.m_hSelectedHero", Util.arrayIdxToString(idx))
            );
        }

        private boolean isSelectedHeroChanged(Entity playerResource, FieldPath[] changedFieldPaths, int nChangedFieldPaths) {
            for (int f = 0; f < nChangedFieldPaths; f++) {
                FieldPath changedFieldPath = changedFieldPaths[f];
                if (changedFieldPath.equals(fpSelectedHero)) return true;
            }
            return false;
        }

        private int getSelectedHeroHandle(Entity playerResource) {
            return playerResource.getPropertyForFieldPath(fpSelectedHero);
        }

    }

}

