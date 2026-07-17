-- Seed hobbies (image_data left NULL — the frontend falls back to a local
-- stock photo matched by keyword in the hobby name/category).
INSERT INTO app_hobby (ref, name, description, category, cost_tier, space_needed, time_commitment, difficulty, status, content_source, icon) VALUES
  ('seed-hobby-pottery', 'Pottery Fundamentals', 'Learn to center clay and throw your first bowls and mugs on the wheel.', 'Craft', 'MEDIUM', 'DEDICATED', 'MODERATE', 'BEGINNER', 'PUBLISHED', 'MANUAL', '🏺'),
  ('seed-hobby-gardening', 'Urban Balcony Gardening', 'Grow herbs, salad greens, and flowers in containers on a small balcony.', 'Lifestyle', 'LOW', 'MINIMAL', 'LIGHT', 'BEGINNER', 'PUBLISHED', 'MANUAL', '🌱'),
  ('seed-hobby-sourdough', 'Sourdough Bread Baking', 'Grow a starter from scratch and bake your first crusty sourdough loaf.', 'Culinary', 'LOW', 'MODERATE', 'MODERATE', 'INTERMEDIATE', 'PUBLISHED', 'MANUAL', '🍞'),
  ('seed-hobby-baking', 'Home Baking Basics', 'Master the fundamentals of measuring, mixing, and baking simple pastries.', 'Culinary', 'LOW', 'MINIMAL', 'LIGHT', 'BEGINNER', 'PUBLISHED', 'MANUAL', '🍰'),
  ('seed-hobby-photography', 'Film Photography', 'Shoot your first roll of 35mm film and learn to read light like a pro.', 'Art', 'MEDIUM', 'MINIMAL', 'LIGHT', 'BEGINNER', 'PUBLISHED', 'MANUAL', '📷'),
  ('seed-hobby-cycling', 'Night Cycling', 'Build the gear, safety habits, and routes for confident night rides.', 'Fitness', 'MEDIUM', 'MINIMAL', 'MODERATE', 'INTERMEDIATE', 'PUBLISHED', 'MANUAL', '🚴')
ON CONFLICT (ref) DO NOTHING;

-- Stages + steps per hobby, resolved via the hobby's unique ref.

-- Pottery Fundamentals
INSERT INTO app_stage (ref, title, "order", hobby_id)
SELECT 'seed-stage-pottery-1', 'Getting to Know Clay', 1, h.id FROM app_hobby h WHERE h.ref = 'seed-hobby-pottery'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_stage (ref, title, "order", hobby_id)
SELECT 'seed-stage-pottery-2', 'Throwing on the Wheel', 2, h.id FROM app_hobby h WHERE h.ref = 'seed-hobby-pottery'
ON CONFLICT (ref) DO NOTHING;

INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-pottery-1-1', 'Wedge your clay', 'Knead the clay on a canvas mat using a spiral motion to remove air bubbles. This prevents the piece from exploding in the kiln later.', 1, 15, s.id FROM app_stage s WHERE s.ref = 'seed-stage-pottery-1'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-pottery-1-2', 'Center the clay', 'Cone the clay up and down on the wheel head with wet hands until it stops wobbling. Centering is the foundation every good throw is built on.', 2, 20, s.id FROM app_stage s WHERE s.ref = 'seed-stage-pottery-1'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-pottery-2-1', 'Open the form', 'Push your thumbs into the centered clay to create the base of your piece, leaving about a half inch of clay at the bottom.', 1, 10, s.id FROM app_stage s WHERE s.ref = 'seed-stage-pottery-2'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-pottery-2-2', 'Pull the walls up', 'Compress and pull the clay upward in even strokes to thin and raise the walls of your bowl or mug.', 2, 20, s.id FROM app_stage s WHERE s.ref = 'seed-stage-pottery-2'
ON CONFLICT (ref) DO NOTHING;

-- Urban Balcony Gardening
INSERT INTO app_stage (ref, title, "order", hobby_id)
SELECT 'seed-stage-gardening-1', 'Choosing Your Setup', 1, h.id FROM app_hobby h WHERE h.ref = 'seed-hobby-gardening'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_stage (ref, title, "order", hobby_id)
SELECT 'seed-stage-gardening-2', 'Planting & Care', 2, h.id FROM app_hobby h WHERE h.ref = 'seed-hobby-gardening'
ON CONFLICT (ref) DO NOTHING;

INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-gardening-1-1', 'Assess your light', 'Track how many hours of direct sun your balcony gets. Most herbs and salad greens need at least 4-6 hours to thrive.', 1, 10, s.id FROM app_stage s WHERE s.ref = 'seed-stage-gardening-1'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-gardening-1-2', 'Choose your containers', 'Pick pots with drainage holes at least 20cm deep for herbs, deeper for anything with roots like tomatoes.', 2, 15, s.id FROM app_stage s WHERE s.ref = 'seed-stage-gardening-1'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-gardening-2-1', 'Plant your first seeds', 'Sow basil, lettuce, or radish seeds at the depth listed on the packet, and water gently with a fine spray.', 1, 20, s.id FROM app_stage s WHERE s.ref = 'seed-stage-gardening-2'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-gardening-2-2', 'Set a watering rhythm', 'Check soil moisture daily by pressing a finger an inch deep — water only when it feels dry.', 2, 5, s.id FROM app_stage s WHERE s.ref = 'seed-stage-gardening-2'
ON CONFLICT (ref) DO NOTHING;

-- Sourdough Bread Baking
INSERT INTO app_stage (ref, title, "order", hobby_id)
SELECT 'seed-stage-sourdough-1', 'Growing a Starter', 1, h.id FROM app_hobby h WHERE h.ref = 'seed-hobby-sourdough'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_stage (ref, title, "order", hobby_id)
SELECT 'seed-stage-sourdough-2', 'Your First Loaf', 2, h.id FROM app_hobby h WHERE h.ref = 'seed-hobby-sourdough'
ON CONFLICT (ref) DO NOTHING;

INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-sourdough-1-1', 'Mix your starter', 'Combine equal parts flour and water in a jar and stir until smooth. Leave it loosely covered at room temperature.', 1, 10, s.id FROM app_stage s WHERE s.ref = 'seed-stage-sourdough-1'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-sourdough-1-2', 'Feed it daily', 'Discard half the starter and feed with fresh flour and water once a day until it doubles in size within 6-8 hours.', 2, 10, s.id FROM app_stage s WHERE s.ref = 'seed-stage-sourdough-1'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-sourdough-2-1', 'Mix and bulk ferment', 'Combine active starter, flour, water, and salt, then let the dough rest and rise for 4-6 hours, folding occasionally.', 1, 30, s.id FROM app_stage s WHERE s.ref = 'seed-stage-sourdough-2'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-sourdough-2-2', 'Shape, proof, and bake', 'Shape the dough into a tight ball, proof overnight in the fridge, then bake in a hot dutch oven until deep golden brown.', 2, 60, s.id FROM app_stage s WHERE s.ref = 'seed-stage-sourdough-2'
ON CONFLICT (ref) DO NOTHING;

-- Home Baking Basics
INSERT INTO app_stage (ref, title, "order", hobby_id)
SELECT 'seed-stage-baking-1', 'Kitchen Essentials', 1, h.id FROM app_hobby h WHERE h.ref = 'seed-hobby-baking'
ON CONFLICT (ref) DO NOTHING;

INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-baking-1-1', 'Measure like a baker', 'Use a kitchen scale instead of cups for flour and sugar — baking is chemistry, and weight is far more accurate than volume.', 1, 10, s.id FROM app_stage s WHERE s.ref = 'seed-stage-baking-1'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-baking-1-2', 'Bake simple cookies', 'Cream butter and sugar, mix in the dry ingredients, and bake a small batch of cookies to practice oven timing.', 2, 25, s.id FROM app_stage s WHERE s.ref = 'seed-stage-baking-1'
ON CONFLICT (ref) DO NOTHING;

-- Film Photography
INSERT INTO app_stage (ref, title, "order", hobby_id)
SELECT 'seed-stage-photography-1', 'Loading Your Camera', 1, h.id FROM app_hobby h WHERE h.ref = 'seed-hobby-photography'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_stage (ref, title, "order", hobby_id)
SELECT 'seed-stage-photography-2', 'Shooting Your First Roll', 2, h.id FROM app_hobby h WHERE h.ref = 'seed-hobby-photography'
ON CONFLICT (ref) DO NOTHING;

INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-photography-1-1', 'Load the film', 'In a dim room, thread the film leader onto the take-up spool and close the back of the camera, then wind to frame one.', 1, 10, s.id FROM app_stage s WHERE s.ref = 'seed-stage-photography-1'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-photography-2-1', 'Meter the light', 'Use your camera''s built-in meter or a phone app to find the right shutter speed and aperture for the scene.', 1, 15, s.id FROM app_stage s WHERE s.ref = 'seed-stage-photography-2'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-photography-2-2', 'Shoot 36 frames', 'Take your camera out and finish the roll, paying attention to composition rather than chimping a screen you don''t have.', 2, 90, s.id FROM app_stage s WHERE s.ref = 'seed-stage-photography-2'
ON CONFLICT (ref) DO NOTHING;

-- Night Cycling
INSERT INTO app_stage (ref, title, "order", hobby_id)
SELECT 'seed-stage-cycling-1', 'Gear Up Safely', 1, h.id FROM app_hobby h WHERE h.ref = 'seed-hobby-cycling'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_stage (ref, title, "order", hobby_id)
SELECT 'seed-stage-cycling-2', 'Your First Night Ride', 2, h.id FROM app_hobby h WHERE h.ref = 'seed-hobby-cycling'
ON CONFLICT (ref) DO NOTHING;

INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-cycling-1-1', 'Install lights', 'Mount a white front light and red rear light, both visible from at least 100 meters away.', 1, 15, s.id FROM app_stage s WHERE s.ref = 'seed-stage-cycling-1'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-cycling-1-2', 'Add reflective gear', 'Wear a reflective vest or ankle bands so drivers can see your movement, not just a static light.', 2, 10, s.id FROM app_stage s WHERE s.ref = 'seed-stage-cycling-1'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-cycling-2-1', 'Plan a familiar route', 'Choose a well-lit route you already know well during the day for your first ride out.', 1, 10, s.id FROM app_stage s WHERE s.ref = 'seed-stage-cycling-2'
ON CONFLICT (ref) DO NOTHING;
INSERT INTO app_step (ref, title, content, "order", estimated_minutes, stage_id)
SELECT 'seed-step-cycling-2-2', 'Ride with a buddy', 'Bring a friend along for your first few night rides until you''re comfortable with visibility and handling in the dark.', 2, 45, s.id FROM app_stage s WHERE s.ref = 'seed-stage-cycling-2'
ON CONFLICT (ref) DO NOTHING;
